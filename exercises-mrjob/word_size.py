from mrjob.job import MRJob

class WordCount (MRJob):

  def mapper (self, key, line): # `key` will be `None`
    for word in line.split():
      yield len(word), 1

  def reducer (self, key, valuelist):
    yield key, sum(valuelist)

if __name__ == '__main__':
  WordCount.run()
