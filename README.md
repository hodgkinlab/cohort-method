<h3>Hi there,</h3>

<p>This repository contains an application for visualising cell proliferation kinetics using a basic implementation of the Cohort method. This application was written in Java and the source code is also provided.<p>

<p>It is assumed that you are familiar with lymphocyte proliferation assays using division tracking dyes (such as CellTrace<sup>TM</sup> Violet), and basic principles of the method. You can get an overview of these ideas from the <a href="https://github.com/hodgkinlab/cohort-method/blob/master/documents/Kan_cohort_poster.pdf">poster</a> located in this repository, and you can get a deeper understanding of the method from publications [1,2,3].<p>

<p>For any further enquires regarding either using or developing this software please contact <a href="mailto:akan@wehi.edu.au">Andrey Kan</a> from the laboratory of <a href="http://www.wehi.edu.au/people/phil-hodgkin">Professor Phil Hodgkin</a> at the <a href="http://www.wehi.edu.au/">Walter and Eliza Hall Institute</a>.<p>

<h3>For Application Users</h3>
<p>This is a Java based application, so you need a Java virtual machine (JVM) installed in order to run it. JVM is a very common platform and there is a good chance that you already have it in your system.<p>

<p>Everything you need is located in the <code>application</code> folder. Once you have the JVM, you can run the application file which is <code>cohort_app_2.0.0.jar</code>. When you run it for the first time, you will be asked for an input file. As an example, you can use <code>sample data input.xlsx</code> provided with the application.<p>

<p>The program will then visualise basic cohort plots. Note that each time the data is loaded and plotted, the plots are automatically saved as PDF files in the folder with your input data file. You can load a different dataset by using "Load Data" button. On exit, the application remembers the last open file and attempts to re-open it in the next run.<p>

<p>The <b>data format</b> is explained in the provided <code>sample data input.xlsx</code>. This is essentially the same data format introduced in publication [3] for the <a href="https://github.com/hodgkinlab/destinypaper">modelling software</a>. The difference is that the conditions can have text labels, and that only the live cell counts are used in the Cohort method.<p>

<p>Finally, note that this software implements a basic form of the Cohort method. There is much more depth to the method, such as handling the proportion of cells that didn't get activated or using a parametric method for estimating mean division number. We hope to implement more features in future releases.</p>

<h3>For Developers</h3>
The source code has been developed using NetBeans and Apache Mave build manager. In order to build the application run <code>cohort_build_jar.bat</code> in the main folder. Among other things, this will generate a standalone Java application <code>proliferation-cohort-2.0.0-jar-with-dependencies.jar</code> in <code>proliferation-cohort/target</code> folder.

The code is organised into several projects and the main project for this application is <code>proliferation-cohort</code>. Some of the classes from other projects are used in the main project. Note that there is a lot of code/classes that are currently not used, but some of them will be involved in future releases.

To work in NetBeans, run NetBeans and open the project from the main folder. This project is called <code>proliferation</code> and it contains the four projects mentioned above as modules, click on <code>proliferation-cohort</code> to open it as a separate project. Set <code>proliferation-cohort</code> as the main project, and set <code>edu.wehi.celcalc.cohort.gui.application.CohortApplicationController</code> as the main class. Finally, build <code>proliferation-cohort</code> with dependencies after which you should be able to run and debug the application from NetBeans.

There is not much documentation on individual classes, but the code for the basic cohort application part is mostly straightforward. Feel free to ask the contact person nominated above for further questions.

<h3>Acknowledgements</h3>
This software was developed by Derrick Futschik and Andrey Kan
with contributions from Julia Marchingo, Jie Zhou, Su Heinzel, Vanessa Bryant, Charlotte Slade, Phil Hodgkin, and other past and present laboratory members. The development was partially funded by <a href="https://www.vlsci.org.au/">The Victorian Life Sciences Computation Initiative</a>.

<h3>References</h3>
1. Gett A.V. and Hodgkin P.D., A cellular calculus for signal integration by T cells, Nature Immunology, 2000<br>
2. Hawkins E.D. et al., Quantal and graded stimulation of B lymphocytes as alternative strategies for regulating adaptive immune responses, Nature Communications, 2013<br>
3. Marchingo J.M. et al. Antigen affinity, costimulation, and cytokine inputs sum linearly to amplify T cell expansion, Science, 2014<br>