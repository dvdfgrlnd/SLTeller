#!/bin/bash
tmux new-session -d -s sessionOne 'code .'
tmux split-window -v -n server 'react-native start'
tmux split-window -h -t server 'react-native log'
tmux split-window -v -n emulator 'emulator -avd Nexus5X'
tmux break-pane -d -s emulator
tmux split-window -v -n studio 'studio.sh'
tmux break-pane -d -s studio
tmux -2 attach-session -d