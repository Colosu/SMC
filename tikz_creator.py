#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Jan 23 13:37:44 2020

@author: colosu
"""

Ops = "Ops.txt"
tikz_ops = "tikz_ops.txt"

file1 = open(Ops, "r")    
target1 = open(tikz_ops, "w")

idx = []
bf = []
ops = []
cap = []
rand = []
vsOps = []
vsCap = []
vsRand = []

for line in file1:
	if line != "\hline\n" and not "|" in line and not "Mean" in line:
		contents = line.split(' & ')
		idx.append(contents[0])
		bf.append(float(contents[1]))
		ops.append(float(contents[2]))
		cap.append(float(contents[3]))
		rand.append(float(contents[4]))
		vsOps.append(float(contents[5]))
		vsCap.append(float(contents[6]))
		vsRand.append(float(contents[7][:-3]))

for i in range(len(idx)):
	target1.write(str(idx[i]) + " " + str(bf[i]) + " " + str(ops[i]) + " " + str(cap[i]) + " " + str(rand[i]) + " " + str(vsOps[i]) + " " + str(vsCap[i]) + " " + str(vsRand[i]) + "\n")
	target1.flush()
	
print("BF mean: " + str(sum(bf)/len(bf)))
print("ops mean: " + str(sum(ops)/len(ops)))
print("cap mean: " + str(sum(cap)/len(cap)))
print("rand mean: " + str(sum(rand)/len(rand)))
print("vsOps mean: " + str(sum(vsOps)/len(vsOps)))
print("vsCap mean: " + str(sum(vsCap)/len(vsCap)))
print("vsRand mean: " + str(sum(vsRand)/len(vsRand)))


Size = "Quality.txt"
tikz_size = "tikz_qa.txt"

file2 = open(Size, "r")    
target2 = open(tikz_size, "w")

idx2 = []
q_5 = []
cap_q_5 = []
rand_q_5 = []
q_25 = []
cap_q_25 = []
rand_q_25 = []
q_set = []
cap_q_set = []
rand_q_set = []

for line in file2:
	if line != "\hline\n" and not "|" in line and not "Total" in line:
		contents = line.split(' & ')
		idx2.append(contents[0])
		q_5.append(float(contents[1]))
		cap_q_5.append(float(contents[2]))
		rand_q_5.append(float(contents[3]))
		q_25.append(float(contents[4]))
		cap_q_25.append(float(contents[5]))
		rand_q_25.append(float(contents[6]))
		q_set.append(float(contents[7]))
		cap_q_set.append(float(contents[8]))
		rand_q_set.append(float(contents[9][:-3]))

for i in range(len(idx)):
	target2.write(str(idx2[i]) + " " + str(q_5[i]) + " " + str(cap_q_5[i]) + " " + str(rand_q_5[i])
		 + " " + str(q_25[i]) + " " + str(cap_q_25[i]) + " " + str(rand_q_25[i])
		 + " " + str(q_set[i]) + " " + str(cap_q_set[i]) + " " + str(rand_q_set[i]) + "\n")
	target2.flush()

print("q_5 mean: " + str(sum(q_5)/len(q_5)))
print("cap_q_5 mean: " + str(sum(cap_q_5)/len(cap_q_5)))
print("rand_q_5 mean: " + str(sum(rand_q_5)/len(rand_q_5)))
print("q_25 mean: " + str(sum(q_25)/len(q_25)))
print("cap_q_25 mean: " + str(sum(cap_q_25)/len(cap_q_25)))
print("rand_q_25 mean: " + str(sum(rand_q_25)/len(rand_q_25)))
print("q_set mean: " + str(sum(q_set)/len(q_set)))
print("cap_q_set mean: " + str(sum(cap_q_set)/len(cap_q_set)))
print("rand_q_set mean: " + str(sum(rand_q_set)/len(rand_q_set)))