#!/bin/bash

# times delay instances

./benchmark_count_strings.sh 10 0 1

./benchmark_count_strings.sh 10 0 4

./benchmark_count_strings.sh 10 0 8

./benchmark_count_strings.sh 10 0 16


./benchmark_count_strings.sh 10 100 1

./benchmark_count_strings.sh 10 100 4

./benchmark_count_strings.sh 10 100 8

./benchmark_count_strings.sh 10 100 16


./benchmark_count_strings.sh 100 0 1

./benchmark_count_strings.sh 100 0 4

./benchmark_count_strings.sh 100 0 8

./benchmark_count_strings.sh 100 0 16


./benchmark_count_strings.sh 100 100 1

./benchmark_count_strings.sh 100 100 4

./benchmark_count_strings.sh 100 100 8

./benchmark_count_strings.sh 100 100 16