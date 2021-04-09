# Folder _matrices_
This folder contain for each Benchmark and for each bug the matrices computed from mutant test execution as well as information about the mutants (types, locations,...) and the prediction probabilities returned by the machine learning model for each mutant (during cross fold validation). Each file content is presented in the following: 
- **SM.dat** is the matrix for strong mutation. The first row represent the tests names and the first colum the mutant IDs. Each colum represent test case and each row represent a mutant. value 0 for row i and column j mean that the mutant at row i was not killed by test at column j. Value 1 mean the mutant at row i was killed by test at column j.
- **WM.dat** is similar to SM.dat but represent weak mutation.
- **ktestPassFail.txt** is the list of test and their pass/fail verdict. each test is separated by the pass/fail verdict by a space. 0 means test pass and 1 mean test fail (reveal the fault).
- **mutantsInfos.json** is the json file ci=ontaining the information about the mutants. the json object is a key balue where the keys are mutant IDs and the value the corresponding mutant information. Each mutant information is a key value of the information and its value. the informations are: mutant type ('Type'), mutant location ('SrcLoc'), mutant function ('FuncName') and the position of the mutant llvm IR in the function('IRPosInFunc').
- **scoresForEquivalentMutantsDetection.json** contain the list of prediction probability to be equivalent mutant given by the machine learning to predict equivalent mutant. the predictions are
sorted by mutant ID (going from 1 to number of mutant). The first value is for mutant ID = 1,...
- **scoresForFaRMSelection.json** same as _scoresForEquivalentMutantsDetection.json_ but representing the probability be fault revealing mutant.

