
				#CryptoSystemDemo


This program will simulate encryption methods and demonstrate them working with a java swing gui
by showing two text input fields, one on the left and another the right these will show what Alice and Bob can see respectivley.

The aim is to demonstrate both the RSA cryptosystem as well as the fourway
handshake and a few other methods of encryption and decryption, I also aim
to show their weaknesses by analysing the data that is always intercepted

The box in the middle will show the public cipher text this is all that Charlie can see also and Gui.java will manage this.

Protocol.java will manage messages between other objects, some cryptographic systems requir one message to be passed back and fourth
a few times and so will be needed to be handled with a set protocol

Encryption.java will contain a few decrypting and encrypting functions including deliberatley bad examples or simplified examples.

Charlie.java may contain some functions to analyse some of the public cipher text, this will be implimented if theres time.
