# Known Limitation
- Don't use "_" as part of attribute name (e.g: "lug_boot" will not work, please write "lugboot" instead).
The reason is because I rely on "_" on hashmap keys.

# Prerequisites
- Java 8 (I'm not sure if I actually use Java 8 code or not, but just to be safe please install Java 8).
- In CSV file, please add a column of attribute names on the first row (please see car.csv and play.csv as a reference of how to do it).

# Compilation
- ```javac Main.java```

# Run compiled program
- ```java Main <filepath> <target-index>```, if you're running it without these arguments, it will run the code using `car.csv`. Index starts with 0.