#!/bin/bash
tmux new-session -s sessionOne 'emulator -avd Nexus5X'
tmux split-window -h 'react-native start'
