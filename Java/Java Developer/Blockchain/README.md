# Blockchain

## My review

This project makes me frustrated, although I'm equipped with new knowledge about stream APIs, collectors and many other useful skills of Java Coding.

The goals of different stages are relatively independent and sometimes even interfere with each other. I have to refactor my program from time to time. It's not about upgrade or optimization but about re-inventing the wheels. For example, stage 6 requires changing the implementation of Messages to Transactions and they are very different things.  The overall structure of my program has taken a big step towards "supporting messages". And forcibly switching to "support transfer" has led to many problems in the program.  Another example: in previous stages the project requires me to implement the "save the chain to file" function. I kept maintaining it to stage 6 but the tests in stage 6 requires generating different blockchains every time. So I have to comment out the relative lines to make my program get passed. 

Second, the test of later stages (like stage 4, 5, 6) didn't care about the logic of your program at all. You can pass the test as long as your program outputs the result in similar format in the samples. Even I get passed in all the stages, I'm sure that problem still exists in my program because it has a possibility to get stuck inexplicably. I can't got any help in locating the reasons.