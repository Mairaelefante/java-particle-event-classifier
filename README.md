# Java Particle Event Classifier
Java 17 project that generates synthetic detector events and trains a multiclass softmax-regression model from scratch to classify electrons, muons, pions, kaons and protons.

## Run (Windows PowerShell)
```powershell
.\run.ps1
```
Output: `build/predictions.csv` plus accuracy, confusion matrix, precision, recall and F1.

The data are synthetic and intended for software/ML demonstration, not experimental use.
