from mrjob.job import MRJob

class LetterCount (MRJob):

  def mapper (self, key, line):
		for symbol in line:
			if isletter (symbol):
				yield symbol, 1

	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
	LetterCount.run()