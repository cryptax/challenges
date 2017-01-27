#!/usr/bin/perl
# A. Apvrille - Hashdays Challenge 2012

use strict;
use warnings;
use Getopt::Long;
use Digest::SHA;
use Digest::Adler32;

my $help;
my $dex = {
    filename => '',
    magic => '',
    checksum => '',
    sha1 => ''
};

sub usage {
    print "./unhidex.pl --input <filename>\n";
    exit(0);
}

# little endian to big endian
sub ltob {
    my $hex = shift;

    my $bits = pack("V*", unpack("N*", pack("H*", $hex)));
    return unpack("H*", $bits);
}

sub btol {
    my $hex = shift;
    my $bits = pack("N*", unpack("V*", pack("H*", $hex)));
    return unpack("H*", $bits);
}

# ubyte[8] DEX_FILE_MAGIC = { 0x64 0x65 0x78 0x0a 0x30 0x33 0x35 0x00 }
sub get_magic {
    my $fh = shift;
    my $data;

    read( $fh, $data, 8) or die "cant read magic from file: $!";
    my $hex = unpack( 'H*', $data );
    return $hex;
}

# uint 32-bit unsigned int, little-endian
sub get_checksum {
    my $fh = shift;
    my $data;

    read( $fh, $data, 4) or die "cant read checksum from file: $!";
    my $hex = unpack( 'H*', $data );

    return $hex;
}

sub write_checksum {
    my $filename = shift;
    my $hexstring = shift;

    my @checksum = pack( 'H*' , btol( $hexstring ) );

    open( FILE, "+<$filename" ) or die "cant open file '$filename': $!";
    binmode FILE, ":bytes";
    seek( FILE, 8, 0 );

    foreach my $byte (@checksum) {
	print( FILE $byte ) or die "cant write checksum in file: $!";
    }

    close( FILE );
    
}

sub get_sha1 {
    my $fh = shift;
    my $data;

    read( $fh, $data, 20) or die "cant read checksum from file: $!";
    my $hex = unpack( 'H*', $data );
    return $hex;
}

sub write_sha1 {
    my $filename = shift;
    my $hexstring = shift;
    my @hash = pack( 'H*', $hexstring );
    my $byte;

    open( FILE, "+<$filename" ) or die "cant open file '$filename': $!";
    binmode FILE, ":bytes";
    seek( FILE, 8+4, 0);

    foreach $byte (@hash) {
	print( FILE $byte ) or die "cant write sha1 in file: $!";
    }

    close( FILE );
}

sub compute_dex_sha1 {
    my $filename = shift;
    open( FILE, $filename ) or die "sha1: cant open $filename: $!";
    binmode FILE;

    # skip magic, checksum, sha1
    $dex->{magic} = get_magic(\*FILE);
    $dex->{checksum} = get_checksum(\*FILE);
    $dex->{sha1} = get_sha1(\*FILE);

    # compute sha1 
    my $shaobj = new Digest::SHA("1");
    my $sha1 = $shaobj->addfile(*FILE)->hexdigest;
    close( FILE );

    return $sha1;
}

sub compute_dex_checksum {
    my $filename = shift;
    open( FILE, $filename ) or die "sha1: cant open $filename: $!";
    binmode FILE;
    
    # skip magic and checksum
    get_magic(\*FILE);
    $dex->{checksum} = get_checksum(\*FILE);

    my $a32 = Digest::Adler32->new;
    $a32->addfile(*FILE);
    close(FILE);    
    my $checksum = $a32->hexdigest;

    return $checksum;
}

sub read_uleb128 {
    my $fh = shift;
    my $result = 0;
    my $shift = 0;
    my $byte;
    my $data;
    do {
	read( $fh, $data, 1) or die "cant read byte : $!";
	$byte = unpack( "c", $data );
	#my $hex = unpack( "H*", $data );
	$result |= ($byte & 0x7f) << $shift;
	#my $tmpr_hex = unpack( "H*", pack("c", $result ));
	$shift += 7;
    } while (($byte & 0x80) != 0);

    return $result;
}

sub write_uleb128 {
    my $fh = shift;
    my $result = shift;
    my $value = $result;
    my $byte;

    do {
	$byte = 0x7f & $value;
	#print "7 lower order bits: ".unpack( "H2", pack( "I", $byte ))."\n";
	$value >>= 7;
	if ($value != 0) {
	    $byte |= 0x80;
	}
	#print " => ".unpack( "H2", pack( "C", $byte ))."\n";
	print( $fh pack("C", $byte)) or die "cant write byte: $!";

    } while ($value != 0);
}

sub modify_virtual_methods_size {
    my $filename = shift;

    open( FILE, "+<$filename" ) or die "cant open file '$filename': $!";
    binmode FILE, ":bytes";
    seek( FILE, 0x3988, 0); # SEEK_SET

    # unsigned LEB128, variable-length, 
    my $virtual_methods_size = read_uleb128( \*FILE );
    print "Virtual Methods Size: $virtual_methods_size\n";

    # modify -> set virtual size to 0
    seek( FILE, 0x3988, 0);
    write_uleb128(\*FILE, 1);

    # set beginning of hidden method to 0
    seek( FILE, 0x39b1, 0);
    write_uleb128(\*FILE, 0x22); # method_idx
    write_uleb128(\*FILE, 0x1); # access flags
    write_uleb128(\*FILE, 0x1fcc); # code offset (2 bytes)

    close( FILE );
}


# -------------- Main ------------------
usage if (! GetOptions('help|?' => \$help,
		       'input|i=s' => \$dex->{filename} )
	  or defined $help
	  or $dex->{filename} eq '' );

modify_virtual_methods_size( $dex->{filename} );

my $new_sha1 = compute_dex_sha1( $dex->{filename} );
write_sha1( $dex->{filename}, $new_sha1 );

my $new_checksum = compute_dex_checksum( $dex->{filename} );
write_checksum( $dex->{filename}, $new_checksum );

print "Writing:\n";
print "Checksum: $new_checksum\n";
print "SHA1    : $new_sha1\n";

exit(1);
