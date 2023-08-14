from mrjob.job import MRJob

class Lab3 (MRJob):
  
  def steps (self):
    return [
      MRStep(mapper=self.mapper1,reducer=self.reducer1),
      MRStep(mapper=self.mapper2,reducer=self.reducer2),
    ]
  
	# hint: T=(dest, origin, count)
  # SELECT T2.dest, T1.origin FROM T as T1, T as T2 WHERE T1.dest == T2.origin
  # 
  # -------------------------------
  # DEST   ORIGIN   RANDOM NUMBER
  # US    France   2238203980
  # US    Germany   0238-29809
  # France  Germany 08098234
  # Germany France 09809.343
  # -------------------------------
  
	def mapper1 (self, key, value):
    parts = value.split(",")
    if len(parts) == 3 and parts[2] != "count":
      dest   = parts[0]
      origin = parts[1]
      yield   dest, (origin, "T1")
      yield origin, (dest,   "T2")

  # Possible key, value_it that a reducer might see
  # 1. US  [(Germany, T1), (France, T1)] - "Cannot get out of US"
  # 2. Germany [(US, T2), (France, T2), (France, T1)] -- "France -> US in 2 flights" and "France -> France in 2 flights"
  # 3. France  [(Germany, T2), (US, T2), (France, T1)]
  # "France -> Germany in 2 flights" and "France to US in 2 flights"
      
  def reducer1 (self, key, value_it):
    t1 = []
    t2 = []
    for v in value_it:
      if v[1] == "T2":
        t2.append(v[0]) # list of T2 countries
      else: # v[1] == "T1"
        t1.append(v[0]) # list of T1 countries
    # every T1 country can get to any T2 country in 2 flights
    for k in t1:
      for v in t2:
        yield k,v  #v can be reached from k in 2 flights
        
    # other alternatives (affect the next mapper)
    # yield t1, t2 -- this is preferred, fewer bytes needed
    # for k in t1:
    #     yield k, t2

  # At this point, reducer outputs (k, v) where v is reachable from k in 2  hops.
	#Stage:
	def mapper2 (self, key, value):
		yield key, value  # identity mapper

	def reducer2 (self, key, value_it):
		#key is country 
		#value_it: a "list" of countries reachable from key 
		#    in 2 hops, possibly including duplicates
		yield key, len(set(value_it))

if __name__ == '__main__':
  Lab3.run()