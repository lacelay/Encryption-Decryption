#How to use:

-mode - There are only 2 variants of this argument: dec (decrypt) and enc (encrypt)

-alg - Algorithm type. There are only 2 algorithms: shift (each letter shift in alphabet by entered key) and unicode (shift each letter in unicode table, not in alphabet)

-key - Positive integer, use it to set how far letter would be shifted

-in|-out - in/out file for reading/writing your message

-data - if -in argument not set, use this argument to enter yout message

For example (compile into jar file before):

java -jar Test.jar -mode enc -data "I love computers!" -alg unicode -key 5 -out C:\users\yaroslav\output.txt

In this case, after program was executed, encrypted message "N%qt{j%htruzyjwx&" will be writed into output.txt file;
