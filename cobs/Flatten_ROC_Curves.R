#  Path we are using for the various files here, this is the only item to change in the script
#  This needs to be the output directory from "RocOnBigTable.class"
universal_Path <- '/Users/kkreth/Documents/_results/roc/'

#  This is how we gather the files to loop through
loop_files <- list.files(path = universal_Path, pattern = "\\_*")

        #  Here is where we leverage the list
        for(fILE in loop_files) {
          
#  Here we are reading in our table of interest --- NOTE we are NOT grabbing all columns, makes things faster
infile <- paste(universal_Path,fILE, sep = "")

#This is supposed to limit the rows I get
raw_line <- read.table(infile, header=TRUE, sep="\t",colClasses=c('character', 'NULL','NULL','NULL','NULL','NULL', 'numeric', 'numeric')) 

#  Now I need to be sensible about the levels
lev<-levels(factor(raw_line[ ,1]))

# Now to loop through the levels
for(i in lev){ 
  
  #  First to set our variable for what we want to "sample" the eventual line at:
  x <- seq(0, 1, 0.01)
  #  Here we use a function that will help us scale values - NOTE:  This is generic
  range01 <- function(x){(x-min(x))/(max(x)-min(x))}

#  Now we will add two columns to the table, but "normalizing" (scaling) the values so that we can sample later....makes things that much easier
raw_line$TP<-range01(raw_line[ ,2])
raw_line$FP<-range01(raw_line[ ,3])

#  Note that we are using "generic" terms and still the only value in NEED of change is the first raw_line input.  This is a simple
#  Misdirection so that the table we are working with (data frame technically) requires less discrimination in our code
newColumnLine <- data.frame(raw_line[["FP"]],raw_line[["TP"]])

##  The below is just for debugging
#plot(newColumnLine)

#  Now for a standard way to flatten our data to better graph in better graphing programs
ap_line_notSure <- approx   (newColumnLine,  method="linear", n=100, xout = x, ties = mean)

#  Need this to be a dataframe for sure
ap_line=data.frame(ap_line_notSure);

#  Now we want to append the ID
ap_line["PFAM_ID"] <- i

##  The below is just for debugging
#plot(ap_line)

##  Now to write this out
outfile<-paste(universal_Path,fILE,"_FLAT.dat", sep = "")
write.table(ap_line,file=outfile,sep=",",row.names=F,col.names=F,append=TRUE,quote = FALSE)

} # End of levels Loop
       
        } #  End of Loop for file level
