from mrjob.job import MRJob

class WordRate (MRJob):

	MRJob.SORT_VALUES=True

	def mapper_init (self):
		self.cache = {}
		self.total = 0

	def mapper (self, _, line):
		words = line.split()
		for word in words:
			self.cache[word] = self.cache.get(word, 0) + 1
			self.total += 1
			# flush the cache
			if len(self.cache) > 100:
				for w in self.cache:
					yield w, self.cache[w]
				self.cache = {}

	def mapper_final (self):

		if self.cache: # if len(self.cache) > 0
			for w in self.cache:
				yield w, self.cache[w]
			self.cache = {}

		if self.total: # if self.total > 0
			yield '_Total', self.total
			self.total = 0

	def reducer_init (self):
		self.cache = {}
		self.total = 0

	def reducer (self, key, values):
		if key == '_Total':
			self.total += sum(values)
		else:
			self.cache[key] = self.cache.get(key, 0) + sum(values)

	def reducer_final (self):
		for word in self.cache:
			yield word, self.cache[word] / self.total

if __name__ == '__main__':
	WordRate.run()