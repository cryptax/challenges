# Summary

This challenge consists in finding a flag contained in a USB dongle.
This USB dongle supports Bluetooth Low Energy, and is configured as a beacon.
The advertising packet it sends is crafted with a specific "Manufacturer Specific Data" which contains as UUID/Major/Minor field the flag to find for the CTF.

Obviously, this challenge only works locally.

# Installation Prerequisites

- Linux host
- [Bluez](http://www.bluez.org/download/) v5.41 or +
- BLE 4.0 USB dongle


# Example of Setup

- Compile and install bluez. If anything below works strange, make sure to remove default bluez packages 
and only use the one you compiled.
- Plug in the dongle
```bash
$ lsusb
Bus 002 Device 006: ID 0a12:0002 Cambridge Silicon Radio, Ltd Frontline Test Equipment Bluetooth Device
```
- At first, the device is usually down:
```bash
$ hciconfig
hci0:	Type: BR/EDR  Bus: USB
	BD Address: 00:00:00:00:00:00  ACL MTU: 0:0  SCO MTU: 0:0
	DOWN 
	RX bytes:0 acl:0 sco:0 events:0 errors:0
	TX bytes:0 acl:0 sco:0 commands:0 errors:0
```
- Put it up:
```bash
$ sudo hciconfig hci0 up
```
- You can now use it. For instance, you can try and scan nearby BLE devices:
```bash
sudo hcitool lescan
LE Scan ...
F3:F7:63:78:73:09 (unknown)
F3:F7:63:78:73:09 Flex
```
- Setup the dongle for advertising, not scanning:
```bash
$ sudo hciconfig hci0 leadv 3
$ sudo hciconfig hci0 noscanc
```
- Issue the advertising command:
```bash
$ sudo hcitool -i hci0 cmd 0x08 0x0008 1e 02 01 1a 1a ff 43 00 02 15 47 48 31 36 7b 42 4c 45 2d 2d 72 75 
6c 33 5a 7d 00 00 00 00 aa 00
```
- Keep the dongle plugged in. If you unplug it, you'll have to go back to where you put the device up.

