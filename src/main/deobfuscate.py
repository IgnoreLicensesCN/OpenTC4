import os
from pathlib import Path

import pandas as pd

deobList:list[tuple[str,str]] = []
for i in pd.read_csv('fields.csv').values:
    deobList.append((i[0],i[1]))
for i in pd.read_csv('methods.csv').values:
    deobList.append((i[0],i[1]))


files = os.walk('java')
for fTri in files:
    folder:str = fTri[0]
    fileNames:list[str] = fTri[2]
    for fileName in fileNames:
        if fileName.endswith('.java'):
            filePath = folder+'\\'+fileName
            with open(filePath,mode='r',encoding='utf-8') as f:
                fullText = f.read()
            for obfuscated,deobfuscated in deobList:
                fullText = fullText.replace(obfuscated,deobfuscated)
            with open(filePath,mode='w',encoding='utf-8') as f:
                f.write(fullText)