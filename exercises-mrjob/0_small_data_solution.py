import sys

def word_count (filename):
  counter = {}
  with open(filename) as infile:
    for line in infile:
      words = line.split()
      for w in words:
        counter[w] = counter.get(w, 0) + 1
  return counter

print(word_count(sys.argv[1]))