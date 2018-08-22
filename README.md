# Facundo's Test

## Android Code Test 3

### Notes

Although I have 8+ years of Android development experience it has been practically Java all the time. My Kotlin experienced is resumed in a few lines of contribution when I was working as a front-end developer not responsible for de Android app but in charge of a Web app. I developed this project in Kotlin because it was suggested this way and because in case of not moving forward as candidate at least I move forward with my Kotlin learning experience, which I consider it is just the beginning.
That being said I would like to observe that there are a few tools I used for Java larger production apps that I haven't configured to conserve app light and code lean, for example RxJava (is quite heavy these days) and Dagger 2 (I configured Singleton for Retrofit and it's services). Not to mention a tracking tool.
I left behind enhacements eg. tests, code optimization, transitions, animations, interactions,splash screen..., because I only had a few hours I could dedicate to the project and I wanted develop from zero and deliver a simple but consistent app within a week from receiving the task.

I think that would be all I'd like to add.

Happy Reviewing!

P.S.: I prefer handling HttpIntentService results with a local broadcast rather than a ResulReceiver. This way if I need other component to be notified about the result I just add a BroadcastReceiver. 

P.S.2: I've left an unsolved warning  https://github.com/bumptech/glide/issues/2911