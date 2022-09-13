import pickle
from os.path import dirname, join
from scipy.sparse import hstack

filename = join(dirname(__file__), "second_model.sav")
# input_query = 'Asshole'

def getProb(data):
    arr = [data]
    with open(filename, 'rb') as f:
        model = pickle.load(f)
    test_char_features = model[0].transform(arr)
    test_word_features = model[1].transform(arr)
    test_features = hstack([test_char_features, test_word_features])
    flag = model[2].predict(test_features)
    return str(flag[0])

