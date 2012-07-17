Skype Bot
=============
Getting Started
-----------------------

I have written 2 posts about getting started, these will hold your hand on every step. Check them out
and write your own Skype bot today :)

 * [Skype Bot for Fun and Profit - Intro](http://dow.ngra.de/2012/01/06/skype-bot-for-fun-and-profit/)
 * [Getting Started - everything you need before coding](http://dow.ngra.de/2012/01/06/skype-bot-for-fun-and-profit-part-i-getting-started/)
 * [Getting Running - lets get this puppy on the rode](http://dow.ngra.de/2012/01/06/skype-bot-for-fun-and-profit-part-ii-getting-it-running/)

Building the Software
---------------------

You need to install the lib/skype-sdk-1.0.jar into your maven local repository.
I got the file from http://developer.skype.com/ and it did not have a version
number. I just called it 1.0. Easiest is to run the following command:

    mvn install:install-file -Dfile=lib/skype-sdk-1.0.jar \\
                -DgroupId=skype -DartifactId=skype-sdk -Dversion=1.0 \\
                -Dpackaging=jar

Running the Software
--------------------

You will need:

 * skype username/password
 * join skype developer program for 10 USD at http://developer.skype.com
 * create an application at http://developer.skype.com 
 * download a runtime for that application and keep it running (bot connects to that)
 * download your application certificate from http://developer.skype.com (the bot will use it)

Most configuration can be done by copying project.properties to personal.properties
and making changes there.

If you have any questions just drop me a line [@toomasr](http://twitter.com/#!/toomasr)
