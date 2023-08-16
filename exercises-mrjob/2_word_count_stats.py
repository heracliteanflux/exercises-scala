from mrjob.job import MRJob

class WordCountStats (MRJob):

	def mapper (self, key, line):
		words = line.split()
		for word in words:
			yield '_' + word, 1
			yield 'Total_', 1
			first_letter = word[0].upper()
			yield first_letter + '_', 1

	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
	WordCountStats.run()