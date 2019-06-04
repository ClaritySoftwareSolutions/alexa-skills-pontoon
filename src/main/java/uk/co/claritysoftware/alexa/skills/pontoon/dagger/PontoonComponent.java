package uk.co.claritysoftware.alexa.skills.pontoon.dagger;

import javax.inject.Singleton;
import uk.co.claritysoftware.alexa.skills.pontoon.session.SessionSupport;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.HandlerFactory;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.LaunchHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonGameActions;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.PontoonSpeechlet;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.HelpIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.StartGameIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.StickIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.StopIntentHandler;
import uk.co.claritysoftware.alexa.skills.pontoon.speech.intent.handler.TwistIntentHandler;

import dagger.Component;

/**
 * Dagger 2 component to build Pontoon components
 */
@Singleton
@Component(modules = PontoonModule.class)
public interface PontoonComponent {

	SessionSupport buildSessionSupport();

	PontoonGameActions buildPontoonGameActions();

	PontoonSpeechlet buildPontoonSpeechlet();

	HandlerFactory buildHandlerFactory();

	LaunchHandler buildLaunchHandlerFactory();

	StartGameIntentHandler buildStartGameIntentHandler();

	HelpIntentHandler buildHelpIntentHandler();

	TwistIntentHandler buildTwistIntentHandler();

	StickIntentHandler buildStickIntentHandler();

	StopIntentHandler buildStopIntentHandler();
}
