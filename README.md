#Plague Tracker Tool
#### Demo Hosted at [http://plague.mybluemix.net/](http://plague.mybluemix.net/)

#What It Does
This tool lets you visualize how an infectious disease would spread through the population. It lets you
tune some characteristics about the disease and the simulation, and then it will run a simulation against a
dataset. Finally, it plots the results on a google map. 

###User Guide
*More information can be found in the `report.pdf` file.*

####Description of Input Fields


1. **User id** is the identification number representing a user. This number is unique per individual and is used to
track users.
2. **Time When Infected** is the time the user is believed to have become contagious with the virus. This is the
time the tool will use as a starting point to locate the infected user and map out the other users who became
infected as a result.

3. **Max Infection hops** is the number of times a disease can hop to another user before it loses its efficacy. For
example, user A is the infected person and comes in contact with user B (1st hop). User B then comes in
contact with user C (2nd hop) and user C comes in contact with user D (3rd hop). The idea is that some diseases
may mutate after hopping too many times, losing their deadliness. Most diseases don’t lose their efficacy
though, and so a large value such as 10,000 should generally be entered.
4. **Infection proximity** is the distance that 2 users need to get within each other in order for an infection to
spread. This number varies by disease as some spread within close contact, while others have larger ranges
due to being airborne.
5. **Incubation Time** is the minimum amount of time that a user must carry the disease before they can spread it
to other users. For example, some disease are not contagious until symptoms are shown, which may take
hours or days. However, other diseases are instantly contagious, and so you could enter a value such as 0
minutes to simulate this.
6. **Limit Max Infected Users** is an upper bound on how many users the simulation will infect. Because diseases
can spread exponentially, there’s a possibility that the resulting map of infected users could be saturated with
too many data points, making it unreadable. This parameter simply stops the simulation once this many users
become infected.
7. **Simulation Duration** is how long the simulation will run for. For example, if you entered a start time of
10am, Dec 1st for the originally infected user, and then entered 7 hours for the simulation duration, then when
the simulation finishes running, the map presented to you depicts which users are infected with the disease, at
5pm Dec 1st (7 hours later).


After you submit the form and the app finishes computing the results, it presents a map, with a marker at each
location where an infection spread to a new user. It also draws lines between users, which lets you see who infected
whom. You can also click a marker, to see the userid of whom that marker represents.

####Quick Start for Demo purposes
If you enter userid 55, 66, or 888, the rest of the form fields will be ignored, and precomputed data will be plotted
on the map instantly. If you want to see the app compute results in real time, you’ll need to enter a real userid and start
time.  

**Some suggested values:**  
**userid:** 1  
**time:** 2008-11-01T00:42:35  
**analysis duration:** 00:10  

This was a group project for cmpe 272 at San Jose State University.  
Authors: Amy Chou, Carita Ou, Chris Rehfeld, Liping Sun  
