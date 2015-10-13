package se.martenolsson.lah15;

import android.app.Application;

import com.parse.Parse;

public class ApplicationController extends Application {

		@Override
		public void onCreate(){
			//Parse
			Parse.enableLocalDatastore(this);
			Parse.initialize(this, "VrCJhCigE2n6CAXcYilfkw4hsEuvNkiK6MVXfJAC", "D4ctM4xMjtScCmFSW7xMSBqv49Hw8YbMJslbnYO5");
			//Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        }

}