import sys
from bs4 import BeautifulSoup
import codecs


f = codecs.open("tv8.html", 'r')
html = f.read()
soup = BeautifulSoup(html)

for script in soup(["script", "style"]):
   script.extract()   

continut = soup.get_text()

lines = (line.strip() for line in continut.splitlines())

chunks = (phrase.strip() for line in lines for phrase in line.split("  |"))

continut = '\n'.join(chunk for chunk in chunks if chunk)

print (continut)

