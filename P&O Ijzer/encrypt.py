from Crypto.PublicKey import RSA
import base64

key= open("public","r").read()
pub_key = RSA.importKey(key)
to_encrypt = open("to_encrypt","r").read()
cmd_enc = pub_key.encrypt(to_encrypt,0)[0]

with open("encrypted", "w") as file:
	file.write(cmd_enc.encode("base64"))
