[![Build Status](https://travis-ci.org/ClaritySoftwareSolutions/alexa-skills-pontoon.svg?branch=master)](https://travis-ci.org/ClaritySoftwareSolutions/alexa-skills-pontoon)

----
# alexa-skills-pontoon
### Alexa Skill written in Java to play the card game Pontoon
Pontoon is a card game where the aim of the game is to form a hand whose total value is as near as possible to 21, without going above 21.  
Alexa will take the role of the dealer, and will deal 2 cards to start.

The cards have values: ace can be set as either high or low, worth either 11 or 1. Kings, queens, jacks and tens are worth ten, and the 
remaining cards are worth their face value.

At the start of the game the player is dealt 2 cards. The player can either 'twist' to have another card dealt to their hand,
or they can 'stick' to end the game. If the hand goes over 21, the hand is 'bust' and the games ends.

### Motivation
The motivation for this project was a partially a learning exercise for me; but also as a demonstration of using Google's Dagger 2 dependency
injection framrwork within an Alexa Lambda, and of state management.

In respect of state management, many conversational style Alexa skills require this concept. The Alexa framework manages and provides the session,
but it is up to the developer to use the session to store and retrieve data.
The card game Pontoon is a good simple example of this in that at the start of the game a new deck of cards must be created and put on the session.  
The initial deal modifies the card deck by dealing 2 cards. The 2 cards become the player's hand and this must be stored on the session.  
On each turn, the card deck is modified by dealing a card, and the new card is added to the hand.

In addition to managing the state of the deck of cards and the hand, this example also needs to keep track of and manage game flow, or the current
state of the game. For example, if the game has not been started, the player cannot ask to twist or stick. Equally, if there is a game in play, the
player cannot ask to start a new game. My implementation of this is not particularly elegant, but it does work and proves the concept.
A better solution for this would be the use of a simple state machine or flow engine.

### Development
This project is written as an AWS Lambda in Java, using the Amazon SDK's.  
In addition, it uses Google's Dagger 2 framework and freemarker.

#### freemarker
The freemarker templating library is used to produce the text for the `SpeechletResponse`'s. ie. the sentences that Alexa speaks.  
Initially sentences were constructed in code using `String#format()` and placeholders. This quickly became unwieldy and resulted in some undesired
tight coupling in the unit tests.  
The freemarker library was introduced as it is relatively lightweight, is stable and well understood, and can be instantiated by Dagger and
injected as necessary. This gives the advantages that the spoken sentences are now defined as simple text files outside of core code. (They are
templates files on the classpath). Additionally, the unit tests are not tightly coupled to the generated sentence as the freemarker `Configuration`
instance is injected and can therefore be mocked in tests.

### Alexa Skill Config
Whilst not part of the lambda itself, the Alexa Skill config has been included for completeness in the folder `src/alexa-skill`.

----
Copyright &copy; 2017 [Clarity Software Solutions Limited](https://claritysoftware.co.uk)

