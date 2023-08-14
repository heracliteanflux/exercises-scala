# small data solution
# problems
#   a large file might not fit on one's machine
#   the counter is an associative array of key-value pairs and might not fit

def word_count (filename):
  counter = {}
  with open(filename) as infile:
    for line in infile:
      words = line.split()
      for w in words:
        counter[w] = counter.get(w, 0) + 1
  return counter
