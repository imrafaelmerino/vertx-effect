#!/bin/bash

# times delay instances workers

./benchmark_count_strings.sh 10 0 1 50

./benchmark_count_strings.sh 10 0 4 50

./benchmark_count_strings.sh 10 0 8 50

./benchmark_count_strings.sh 10 0 16 50

./benchmark_count_strings.sh 10 100 1 50

./benchmark_count_strings.sh 10 100 4 50

./benchmark_count_strings.sh 10 100 8 50

./benchmark_count_strings.sh 10 100 16 50


./benchmark_count_strings.sh 100 0 1 50

./benchmark_count_strings.sh 100 0 4 50

./benchmark_count_strings.sh 100 0 8 50

./benchmark_count_strings.sh 100 0 16 50


./benchmark_count_strings.sh 100 100 1 50

./benchmark_count_strings.sh 100 100 4 50

./benchmark_count_strings.sh 100 100 8 50

./benchmark_count_strings.sh 100 100 16 50