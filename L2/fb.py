from mrjob.job import MRJob

class Edge (MRJob):

	def mapper (self, key, line):
		edge=line.split()
		if int(edge[0])%2==0: yield edge[1],1
		
	def reducer (self, key, value_list):
		val=sum(value_list)
		if val>=3: yield key,val

if __name__ == '__main__':
	Edge.run()