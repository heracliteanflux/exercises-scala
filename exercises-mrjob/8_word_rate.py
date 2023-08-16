from mrjob.job import MRJob

class WordRate (MRJob):

	MRJob.SORT_VALUES=True

	def mapper_init (self):
		self.cache = {}
		self.total = 0

	def mapper (self, _, line):
		words = line.split()
		for word in words:
			if word not in self.cache:
				self.cache[word] = 0
			self.cache[word] += 1
			self.total += 1
			if len(self.cache) > 100:
				for w in self.cache:
					yield w, self.cache[w]
				self.cache = {}

	def mapper_final (self):
		yield '_Total', self.total

	def reducer_init (self):
		self.cache = {}
		self.total = 0

	def reducer (self, key, values):
		if key == '_Total':
			self.total += values
		else:
			self.cache[key] = values

	def reducer_final (self):
		for word in self.cache:
			yield word, sum(self.cache[word]) / self.total

if __name__ == '__main__':
	WordRate.run()