from mrjob.job import MRJob

class HW2 (MRJob):
  
  def mapper_init (self):
    self.cache=dict.fromkeys(['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'],0)
    self.seen=[]
    
  def mapper (self,key,line):
    for word in line.split():
      for symbol in word:
        if symbol.isalpha() and symbol.lower() not in self.seen:
          self.cache[symbol.lower()]+=1
          self.seen.append(symbol.lower())
      self.seen=[]
      
  def mapper_final (self):
    for letter in self.cache:
      yield letter,self.cache[letter]
      
  def reducer (self,key,values):
    yield key,sum(values)

if __name__ == '__main__':
  HW2.run()