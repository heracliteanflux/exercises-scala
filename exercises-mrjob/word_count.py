from mrjob.job import MRJob

class WordCount (MRJob):

	def mapper (self, key, line):
		words = line.split()
		for word in words:
			yield word, 1

		# for word in line.split():
		# 	yield word, 1

	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
	WordCount.run()