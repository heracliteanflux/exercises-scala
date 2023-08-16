from mrjob.job import MRJob

class WordCountStats (MRJob):

	def mapper_init (self):
		self.words = {}
		self.stats = {}

	def mapper (self, key, line):
		words = line.split()
		for word in words:
			self.words[word] = self.words.get(word, 0) + 1
			if len(self.words) > 100:
				for w in self.words:
					yield w, self.words[w]
				self.words = {}
			self.stats['Total_'] = self.stats.get('Total_', 0) + 1
			first_letter = word[0].upper()
			self.stats[first_letter + '_'] = self.stats.get(first_letter + '_', 0) + 1

	def mapper_final (self):
		if self.words: # if len(self.words) > 0
			for w in self.words:
				yield w, self.words[w]
			self.words = {}
		self.stats = {}

	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
	WordCountStats.run()