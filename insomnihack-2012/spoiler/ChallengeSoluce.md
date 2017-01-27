1- reverse the code of APK
2- the application displays the congrats text if the entered text matches a fixed value.
This fixed value is SHA 256 hash, that's going to be hard to reverse.

Curiously, the Compute class has a `compute()` method that seems quite useless.
If we have a closer look to this method, it performs AES/CTR with no padding.

First, it decrypts a ciphertext 1.
Then, it decrypts a longer cipher text 2.

But it does the decryption wrongly because it is performing an init each time, so consequently the same counter is going to be re-used.

Consequently, this means that:

```
Ek(ctr) XOR P1 = C1
Ek(ctr) XOR P2 = C2
```

so:

```
C1 XOR C2 = Ek(ctr) XOR P1 XOR Ek(ctr) XOR P2 = P1 XOR P2
```

Let's imagine P1 is null.
Then, `Ek(ctr) XOR P1 = Ek(ctr) = C1`
and then `Ek(ctr) XOR P2 = C1 XOR P2 = C2`
so `P2 = C2 XOR C1`

or another way to do it:

`C1 XOR C2 = P1 XOR P2 = P2.`



