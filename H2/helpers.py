import subprocess
import os

def get_file_info (output_directory):
  """ checks output_directory for part files, returns a list of (partfile name, size)  """
  filenames = subprocess.Popen(["hdfs", "dfs", "-ls", output_directory], stdout=subprocess.PIPE)
  lines = filenames.stdout.readlines()
  partfiles = [x.decode("utf-8").split() for x in lines if b"part" in x]
  info = [(os.path.basename(x[7]),int(x[4])) for x in partfiles]
  return info

def get_contents (output_directory):
  """ gets the contents of the files in output_dir """
  contents = subprocess.Popen(["hdfs", "dfs", "-cat", f"{output_directory}/*"], stdout=subprocess.PIPE)
  data = contents.stdout.read().decode("utf-8")
  return data

def split_contents (data, sizes):
  contents = []
  index = 0
  for i in sizes:
    contents.append(to_key_value_pairs(data[index:index+i]))
    index += i
  return contents

def to_key_value_pairs (line):
  """ converts a string representation of a file to a list of (key, value) """
  lines = line.strip().split("\n")
  kv = [tuple(x.split("\t")) for x in lines]
  return kv

def retrieve_kv (output_directory):
  info = get_file_info(output_directory)
  if len(info) != 0:
    data = get_contents(output_directory)
    file_contents = split_contents(data, [x[1] for x in info])
  else:
    file_contents = []
  return file_contents