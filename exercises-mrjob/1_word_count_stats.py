from mrjob.job  import MRJob
from mrjob.step import MRStep

class WordTotal (MRJob):

	def mapper_word_count (self, key, line):
		words = line.split()
		for word in words:
			yield word, 1

	def reducer_word_count (self, key, values):
		yield key, sum(values)

	def mapper_word_total (self, key, value):
		yield 'Total', value
		first_letter = key[0].upper()
		yield first_letter, value

	def reducer_word_total (self, key, values):
		yield key, sum(values)

	def steps (self):
		return [
			MRStep(
				mapper  = self.mapper_word_count,
				reducer = self.reducer_word_count,
			),
			MRStep(
				mapper  = self.mapper_word_total,
				reducer = self.reducer_word_total,
			),
		]

if __name__ == '__main__':
	WordTotal.run()