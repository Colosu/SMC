#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jan 10 15:14:34 2020

@author: colosu
"""

# Pythono3 code to rename multiple  
# files in a directory or folder 
  
# importing os module 
import os
from shutil import copyfile

i = 1
path = "TestsWM"
os.makedirs(path)
path1 = "Codeflaws"
path2 = "Corebench"

for filename in os.listdir(path1):
    if os.path.isdir(path1 + "/" + filename):
        os.makedirs(path + "/test" + str(i))
        dst = path + "/test" + str(i) + "/WM.dat"
        src = path1 + "/" + filename + "/WM.dat"
        copyfile(src, dst) 
        i += 1
  
for filename in os.listdir(path2):
    if os.path.isdir(path2 + "/" + filename):
        os.makedirs(path + "/test" + str(i))
        dst = path + "/test" + str(i) + "/WM.dat"
        src = path2 + "/" + filename + "/WM.dat"
        copyfile(src, dst) 
        i += 1