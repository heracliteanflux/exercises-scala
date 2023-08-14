import pytest
import helpers
output_directory = "hw2output"

@pytest.fixture
def output_data (output_directory=output_directory):
  contents = helpers.retrieve_kv(output_directory)
  return contents

def test_one (output_data):
  pass
  # assert 1 + 1 == 2

def test_two (output_data):
  pass
  # assert 1 + 2 == 2

def test_three (output_data):
  pass
  # assert len(output_data) == 4
  # if len(output_data) > 0:
  #   for part in output_data:
  #     assert False