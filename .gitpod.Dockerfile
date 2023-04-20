FROM gitpod/workspace-full
SHELL ["/bin/bash", "-c"]

RUN sudo apt-get -qq update
# Install required libraries for Projector + PhpStorm
RUN sudo apt-get -qq install -y python3 python3-pip libxext6 libxrender1 libxtst6 libfreetype6 libxi6
# Install Projector
RUN pip3 install projector-installer
# Install PhpStorm
RUN mkdir -p ~/.projector/configs  # Prevents projector install from asking for the license acceptance
# from evernote:///view/58684830/s346/6abd6239-d8a1-ece0-5032-2828777b7e83/0c2e09b1-b749-446b-d20c-a611a6093fe2
RUN projector ide autoinstall --config-name Idea --ide-name "IntelliJ IDEA Ultimate 2023.1"
