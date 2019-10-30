/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GENDLE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.HarddriveStorage;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.workflowsim.CondorVM;
import org.workflowsim.Job;
import org.workflowsim.WorkflowDatacenter;
import org.workflowsim.WorkflowEngine;
import org.workflowsim.WorkflowPlanner;
import org.workflowsim.utils.ClusteringParameters;
import org.workflowsim.utils.OverheadParameters;
import org.workflowsim.utils.Parameters;
import org.workflowsim.utils.ReplicaCatalog;

/**
 *
 * @author soft29
 */
public class ProposedGADE {

    static final String url = "jdbc:mysql://localhost:3306/";
    static final String driver = "com.mysql.jdbc.Driver";
    static final String dbname = "Gendle";
    static final String uname = "root";
    static final String upass = "";
    String username, userpass;

    static int vmNum, hostNum, numberoftask;

    ////////////////////////// STATIC METHODS ///////////////////////
    protected static List<CondorVM> createVM(int userId, int vms) {

        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<CondorVM> list = new LinkedList<>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        double mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        CondorVM[] vm = new CondorVM[vms];
        Random bwRandom = new Random(System.currentTimeMillis());
        for (int i = 0; i < vms; i++) {
            double ratio = bwRandom.nextDouble();
            vm[i] = new CondorVM(i, userId, mips * ratio, pesNumber, ram,
                    (long) (bw * ratio), size, vmm, new CloudletSchedulerSpaceShared());
            list.add(vm[i]);
        }
        return list;
    }

    public static String process(File daxFile, int vms, int hosts) {
        String totalcloudlets = null;
        try {
            // First step: Initialize the WorkflowSim package. 

            String pathval = daxFile.toString();

            String[] ss = pathval.split("/dax");

            Matcher m = Pattern.compile("\\d+").matcher(ss[1]);
            int task = 0;
            while (m.find()) {
//   System.out.println("out===="+m.group(0));

                task = Integer.parseInt(m.group(0));

            }
            vmNum = vms;
            hostNum = hosts;
            numberoftask = task;
            System.out.println("numberoftaskS ==> " + numberoftask);
            String daxPath = pathval;

            if (!daxFile.exists()) {
                Log.printLine("Warning: Please select the daxPath with the physical path in your working environment!");
                //return;
                System.exit(0);
            }

            String inputFolder = System.getProperty("user.dir");

            BufferedWriter writer1 = new BufferedWriter(new FileWriter(inputFolder
                    + "\\Performance\\ProposedGADE\\Result_" + numberoftask + ".txt", true));

            writer1.write("\n");
            writer1.write("\n");
            writer1.write("Number of Hosts =" + hostNum);

            writer1.write("\n");
            writer1.write("Number of Vms = " + vmNum);
            writer1.write("\n");
            writer1.write("Number of Taks=" + numberoftask);

            writer1.write("\n");
            writer1.write("\n");
            writer1.close();

            /**
             * Since we are using MINMIN scheduling algorithm, the planning
             * algorithm should be INVALID such that the planner would not
             * override the result of the scheduler
             */
            Parameters.SchedulingAlgorithm sch_method = Parameters.SchedulingAlgorithm.STATIC;
            Parameters.PlanningAlgorithm pln_method = Parameters.PlanningAlgorithm.GADE;
            ReplicaCatalog.FileSystem file_system = ReplicaCatalog.FileSystem.LOCAL;

            /**
             * No overheads
             */
            OverheadParameters op = new OverheadParameters(0, null, null, null, null, 0);;

            /**
             * No Clustering
             */
            ClusteringParameters.ClusteringMethod method = ClusteringParameters.ClusteringMethod.NONE;
            ClusteringParameters cp = new ClusteringParameters(0, 0, method, null);

            /**
             * Initialize static parameters
             */
            Parameters.init(vmNum, daxPath, null,
                    null, op, cp, sch_method, pln_method,
                    null, 0);
            ReplicaCatalog.init(file_system);

            // before creating any entities.
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            WorkflowDatacenter datacenter0 = createDatacenter("GADE-Schedule", hostNum);

            /**
             * Create a WorkflowPlanner with one schedulers.
             */
            WorkflowPlanner wfPlanner = new WorkflowPlanner("planner_0", 1);
            /**
             * Create a WorkflowEngine.
             */
            WorkflowEngine wfEngine = wfPlanner.getWorkflowEngine();
            /**
             * Create a list of VMs.The userId of a vm is basically the id of
             * the scheduler that controls this vm.
             */
            List<CondorVM> vmlist0 = createVM(wfEngine.getSchedulerId(0), Parameters.getVmNum());

            /**
             * Submits this list of vms to this WorkflowEngine.
             */
            wfEngine.submitVmList(vmlist0, 0);

            /**
             * Binds the data centers with the scheduler.
             */
            wfEngine.bindSchedulerDatacenter(datacenter0.getId(), 0);

            CloudSim.startSimulation();

            List<Job> outputList0 = wfEngine.getJobsReceivedList();
            CloudSim.stopSimulation();

            totalcloudlets = getCloudletList(outputList0);
            printJobList(outputList0, numberoftask);

        } catch (Exception e) {
            Log.printLine("The simulation has been terminated due to an unexpected error" + e);
        }
        return totalcloudlets;
    }

    protected static WorkflowDatacenter createDatacenter(String name, int host) {
        int numofHost = host;
        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<>();

        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating
        //    a Machine.
        for (int i = 1; i <= numofHost; i++) {
            List<Pe> peList1 = new ArrayList<>();
            int mips = 10000;
            // 3. Create PEs and add these into the list.
            //for a quad-core machine, a list of 4 PEs is required:
            peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
            peList1.add(new Pe(1, new PeProvisionerSimple(mips)));

            int hostId = 0;
            int ram = 10240; //host memory (MB)
            long storage = 1000000; //host storage
            int bw = 100000;
            hostList.add(
                    new Host(
                            hostId,
                            new RamProvisionerSimple(ram),
                            new BwProvisionerSimple(bw),
                            storage,
                            peList1,
                            new VmSchedulerTimeShared(peList1))); // This is our first machine
            //hostId++;
        }

        // 4. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 1.50;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.1;	// the cost of using storage in this resource
        double costPerBw = 0.1;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<>();	//we are not adding SAN devices by now
        WorkflowDatacenter datacenter = null;

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        // 5. Finally, we need to create a storage object.
        /**
         * The bandwidth within a data center in MB/s.
         */
        int maxTransferRate = 15;// the number comes from the futuregrid site, you can specify your bw

        try {
            // Here we set the bandwidth to be 15MB/s
            HarddriveStorage s1 = new HarddriveStorage(name, 1e12);
            s1.setMaxTransferRate(maxTransferRate);
            storageList.add(s1);
            datacenter = new WorkflowDatacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datacenter;
    }

    public static String getCloudletList(List<Job> list) {
        int size = list.size();

        Job cloudlet;

        String indent = "    ";
        Log.printLine();
        //  Log.printLine("========== OUTPUT ==========");
        //  Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
        //  + "Data center ID" + indent + "VM ID" + indent);

        String total = "Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time";

        total = total + "\n";

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                // Log.print(indent + cloudlet.getCloudletId() + indent + indent);
                total = total + indent + cloudlet.getCloudletId() + indent + indent;

                // Log.print("SUCCESS");
                total = total + "SUCCESS";
                //    Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId());

                total = total + indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() + indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent + dft.format(cloudlet.getFinishTime());
                ;

                total = total + "\n";
            }
        }

        return total;
    }

    /**
     * Prints the job objects
     *
     * @param list list of jobs
     */
    protected static void printJobList(List<Job> list, int totaltask) throws IOException {
        int noofTasks = totaltask;
        try {

            String inputFolder = System.getProperty("user.dir");

            BufferedWriter writer1 = new BufferedWriter(new FileWriter(inputFolder + "\\Performance\\ProposedGADE\\Result_" + totaltask + ".txt", true));

            int size = list.size();
            Job job;
            int count = 0;

            String indent = "    ";
            Log.printLine();
            Log.printLine("========== OUTPUT ==========");
            Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                    + "Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time" + indent + "Depth");

            DecimalFormat dft = new DecimalFormat("###.##");
            double makespan = 0.0;
            double WaitTime = 0.0;
            double Cost = 0.0;

            for (Iterator<Job> it = list.iterator(); it.hasNext();) {
                Job job1 = it.next();
            }

            for (int i = 0; i < size; i++) {

                job = list.get(i);
                Log.print(indent + job.getCloudletId() + indent + indent);
                if (job.getCloudletStatus() == Cloudlet.SUCCESS) {
                    Log.print("SUCCESS");
                    Log.printLine(indent + indent + job.getResourceId() + indent + indent + indent + job.getVmId()
                            + indent + indent + indent + dft.format(job.getActualCPUTime())
                            + indent + indent + dft.format(job.getExecStartTime()) + indent + indent + indent
                            + dft.format(job.getFinishTime()) + indent + indent + indent + job.getDepth());
                } else if (job.getCloudletStatus() == Cloudlet.FAILED) {
                    Log.print("FAILED");

                    Log.printLine(indent + indent + job.getResourceId() + indent + indent + indent + job.getVmId()
                            + indent + indent + indent + dft.format(job.getActualCPUTime())
                            + indent + indent + dft.format(job.getExecStartTime()) + indent + indent + indent
                            + dft.format(job.getFinishTime()) + indent + indent + indent + job.getDepth());
                }

                makespan = job.getFinishTime();

            }
            Cost = makespan * 1.55;
            writer1.write("Makespan=" + makespan + "s");

            writer1.write("\n");
            writer1.write("\n");
            writer1.close();

            //Connect to database and insert the data
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url + dbname, uname, upass);
            Statement st = con.createStatement();
            System.out.println("Got DB Connection");
            String stm = "INSERT INTO GADEMakespan(NumberOfTasks,Makespan) VALUES('" + noofTasks + "','" + makespan + "')";
            String stm1 = "INSERT INTO GADEExpense(NumberOfTasks,cost) VALUES('" + noofTasks + "','" + Cost + "')";

            st.executeUpdate(stm);
            st.executeUpdate(stm1);
//            System.out.println("Registered in userdb");
            con.close();
            st.close();
//            JOptionPane.showMessageDialog(null, "Hosts are configured and created");
        } catch (Exception e) {

            System.out.println(e);
        }
    }

}
