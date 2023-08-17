from   mrjob.job import MRJob
import string

class LetterCount (MRJob):
	
	def mapper_init (self):
		# initialize a dictionary (key = letter, value = how many times the letter has been seen so far)
		self.cache = dict.fromkeys(string.ascii_letters, 0)
		
	def mapper (self, key, line):
		self.set_status('heartbeat') # What would happen if the hearbeat message is not sent?
		for symbol in line:
			if symbol.isalpha():
				self.cache[symbol] += 1
				
	def mapper_final (self):
		for key, value in self.cache.items():
			yield key, value
		self.cache = {}
		
	def reducer (self, key, values):
		yield key, sum(values)

if __name__ == '__main__':
  LetterCount.run()