# This code exists to confirm my hunch that the optimal position for Part 2 of
# this problem is the mean of all of the initial positions.
# Note that the optimal position is _specifically_ the floor of the mean
# (or at least seems to be by observation).
import random

# Sum of numbers 1->n.
def gauss_sum_formula(n: int) -> int:
    return (n * (n+1)) // 2

def list_mean(l) -> float:
    return sum(l) / len(l)

def main():
    r = range(0, 1001)
    nums = random.sample(r, 25)
    mean_frac = list_mean(nums)
    mean = int(mean_frac) # a.k.a floor
    mean_round = round(mean_frac)
    print(f'NUMS: {nums}')
    print(f'MEAN: {mean_frac} (int={mean}, round={mean_round})')
    fuels = []
    for i in r:
        fuel = sum([gauss_sum_formula(abs(i-n)) for n in nums])
        fuels.append((i, fuel))
        # print(f'POS: {i} FUEL {fuel}')
    min_val = min(fuels, key = lambda v: v[1])
    print(f'MIN: {min_val}')

if __name__ == '__main__':
    main()