from mrjob.job import MRJob

class WordCount (MRJob):

	def mapper_init (self):
		self.cache = {}

	def mapper (self, key, line):
		words = line.split()
		for word in words:
			self.cache[word] = self.cache.get(word, 0) + 1
			# if word not in self.cache:
			# 	self.cache[word] = 0
			# self.cache[word] += 1
			if len(self.cache) > 100:
				for w in self.cache:
					yield w, self.cache[w]
				self.cache = {}

	def mapper_final (self):
		if self.cache: # if len(self.cache) > 0
			for w in self.cache:
				yield w, self.cache[w]
			self.cache = {}

	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
	WordCount.run()