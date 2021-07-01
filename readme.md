# Triple Todo
[![pipeline status](https://gitlab.rotate-it.be/tripled/triple-todo/badges/master/pipeline.svg)](https://gitlab.rotate-it.be/tripled/triple-todo/pipelines)
[![coverage report](https://gitlab.rotate-it.be/tripled/triple-todo/badges/master/coverage.svg)](https://gitlab.rotate-it.be/tripled/triple-todo/commits/master)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)
[![Slack Widget](https://img.shields.io/badge/Slack-Opentripled-blue.svg?style=flat-square)](https://tripled-io.slack.com/messages/opentripled)
[Jacoco report](https://tripled.pages.gitlab.rotate-it.be/triple-todo/)

## Background

The new big shot middle manager at BigBank Incorporated got tasked with improving the efficiency of the employees.
He doesn't trust the employees whatsoever. He wants to micromanage each and every second of their time.
His vision is that there are very clear `tasks` that the resources need to complete.
They can't create tasks themselves and just work on them: He wants to approve tasks to keep everything under control.
When the resource indicates his task is done, he also wants to evaluate the result.

Later on he wants to have fancy business reports and dashboards, integrations with accounting, payroll and invoicing.

The name of this grandiose project: `Zira next-gen 2020`

## This is where you come in

In the past they had some software projects that were outsourced to "RandomITConsultantsInc".
Delivery was slow, and after 1.5 years and the first integrations they started talking about things like legacy, big ball of mud, green field rewrite.

So now, there's "Triple C" ("consultants, cash and cars") comes in.
Apparently they have a magical technique called "Clean architecture" that will solve all of BigBank's problems.

They didn't want to start with a huge analysis document, but something called an "eventstorming".
Drudgingly some managers got dragged up into a big meeting room and some hipsters were waiting with Post-Its.
We answered questions, got into discussions and they apparently found it useful enough to start development...

They were negotiating about the things we want, we don't understand why, but they wanted to "slice" things up.
We just need the task system, it's not a piece of cake!

### Event storming result
![event storming](eventstorming.png "event storming")

### User story result
![event story mapping](user story mapping.png "user story mapping")

### Some notes from development
#### Event storming command "gaps"
While an eventstorming brings out the most important commands, some common ones, like the update of certain information might go missing as it is sometimes too obvious to mention if it doesn't have any business side-effects. This is what happened to the Update Information In Todo Item command.

#### Event storming queries
The flow will also lack the queries - the questions you can ask your system and the state that you can return. This is usually backtracked from what is need for the next command and other information that your users need to see. It's needlessly complex to dig out all this information beforehand, and it is easily fleshed out while working on the api.
