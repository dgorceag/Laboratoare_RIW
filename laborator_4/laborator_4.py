import os
import queue
import _thread
import sys
from bs4 import BeautifulSoup
import codecs
import ntpath
import json
ntpath.basename("a/b/c")


def path_leaf(path):

    head, tail = ntpath.split(path)
    return tail or ntpath.basename(head)


def read_file(text, file, nameFile):

    wordDict = []
    wordCount = dict()
    fileName = path_leaf(file)
    #print()
    for word in text.split():
        flag = 1
        word = word.lower()
        with open('C:\\Users\\dgorc\\Desktop\\Laboratoare_RIW\\laborator_4\\Stopwords.txt', 'r') as stopwords:
            for lineStop in stopwords:

                for stopW in lineStop.split():

                    if(word != stopW):
                        flag = 0

            if(flag == 0):
                #print(word)
                #if()
                try:

                    if word in wordDict:
                        print(word)
                        wordCount[word + ', File: ' + fileName] = wordCount[word + ', File: ' + fileName] + 1
                    else:
                        wordCount[word + ', File: ' + fileName] = 1
                        wordDict.append(word)
                except IndexError:
                    print('no data')

    print(wordDict)
    # print(wordDict)
    # print(wordCount)
    with open(nameFile, 'w') as json_file:
        json.dump(wordCount, json_file)
    
def findText(phrase):
    for word in phrase.split():
        with open('fisier0.json') as json_file:

            for line in json_file.readlines():

                json_dict = json.loads(line)

                for k,v in json_dict.iteritems():
                     if k in keywords.split()[0]:
                        print(k+': find')

def getText(file):
	f = codecs.open(file, 'r')
	html = f.read()
	soup = BeautifulSoup(html)

	for script in soup(["script", "style"]):
	   script.extract()   

	text = soup.get_text()

	lines = (line.strip() for line in text.splitlines())

	chunks = (phrase.strip() for line in lines for phrase in line.split("  ,'"))

	text = '\n'.join(chunk for chunk in chunks if chunk)

	return text


path = 'C:\\Users\\dgorc\\Desktop\\Laboratoare_RIW\\laborator_4\\Sites'

files = []

for root, directories, fisiere in os.walk(path):

    for file in fisiere:
        if '.html' in file:
            files.append(os.path.join(root, file))
fileNr = 0
for htmlFile in files:
    print(htmlFile)
    textHtml = getText(htmlFile)
    jsonName = 'fisier' + str(fileNr) + '.json'
    read_file(textHtml, htmlFile, jsonName)
    fileNr = fileNr + 1

findText('Ultima Ora')
