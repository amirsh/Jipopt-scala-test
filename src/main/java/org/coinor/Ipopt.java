/* 
 * Copyright (C) 2007 VRTech Industrial Technologies - www.vrtech.com.br.
 * All Rights Reserved.
 * This code is published under the Eclipse Public License.
 * 
 * $Id$
 * Authors: Rafael de Pelegrini Soares
 *
 *
 * Copyright (C) 2007 Tong Kewei, Beihang University, - www.buaa.edu.cn.
 * All Rights Reserved.
 * This code is published under the Eclipse Public License.
 * 
 * $Id$
 * This is a revised version of JNI of C++ interface of IPOPT.
 * I changed from Rafael de Pelegrini Soares's original code.
 * His codes are originally derived form C version of IPOPT,which has limited functions. 
 * I derived my codes from C++ version of IPOPT, which is much more powerful.  
 * I also fix a bug in Rafael de Pelegrini Soares's code on function setProblemScaling,
 * In his original code the function setProblemScaling has no use to change problem.
 * I added some useful functions in JIpopt, such as get_scaling_parameters or get_number_of_nonlinear_variables
 * or get_list_of_nonlinear_variables. You can add any more functions as you like. Follow my structure it is 
 * very easy.
 *
 * If you have problem or you need me to add another functions, please contact me.
 *
 * Authors: Tong Kewei, E-mail:tongkewei@126.com
 * Beihang University, website: www.buaa.edu.cn
 * Beijing,China.
 * 2007-11-11
 */

package org.coinor;

import java.io.File;

/**
 * A Java Native Interface for the Ipopt optimization solver.
 * <p>
 * Ipopt is a solver for large scale nonlinear optimization problems (NLP) with 
 * non-linear contraints.
 * <p>
 * The Java Native Interface (JNI) is a programming framework that allows
 * Java code running in the Java Virtual Machine (JVM) to call and be
 * called by native applications (programs specific to a hardware and
 * operating system platform) and libraries written in other languages,
 * such as C and C++.
 * <p>
 * This class is a JNI hook around the C interface of Ipopt, as a consequence
 * it will need a nativelly compiled DLL to run.
 * For more details about Ipopt
 * <a href="https://projects.coin-or.org/Ipopt">click here</a>.
 * <p>
 * The user should subclass this class and implement the abstract methods.
 * At some point before solving the problem the
 * {@link #create(int, double[], double[], int, double[], double[], int, int, int)}
 * function should be called.
 * For simple cases you can call this function in the constructor of your class.
 * <p>
 * Once the problem was created, {@link #solve(double[])} will solve the problem.
 * Objects of this class can be reused to solve different problems, in other words,
 * {@link #create(int, double[], double[], int, double[], double[], int, int, int)}
 * and {@link #solve(double[])} can be called multiple times.
 * <p>
 * Programmers should, for efficiency, call {@link #dispose()} when finished using a
 * Ipopt object, otherwise the nativelly allocated memory will be disposed of only
 * when the JVM call {@link #finalize()} on it.
 * 
 * @author Rafael de Pelegrini Soares, Edson C. do Valle
 *
 * This is a revised version of JNI, a C++ interface of Ipopt, revised by Tong Kewei
 * of BeiHang University.
 * @author Tong Kewei
 */
public abstract class Ipopt {

	/** Native function should not be used directly */
	private native boolean AddIpoptIntOption(long ipopt, String keyword, int val);

	/** Native function should not be used directly */
	private native boolean AddIpoptNumOption(long ipopt, String keyword, double val);

	/** Native function should not be used directly */
	private native boolean AddIpoptStrOption(long ipopt, String keyword, String val);

	/** Native function should not be used directly */
	private native long CreateIpoptProblem(int n,int m, 
			int nele_jac, int nele_hess, int index_style);

	/* Native function should not be used directly */
	private native void FreeIpoptProblem(long ipopt);

	/** Native function should not be used directly */
	private native int OptimizeTNLP(long ipopt, String outfilename,
			double x[], double g[],
			double obj_val[], double mult_g[], double mult_x_L[], double mult_x_U[],
			double callback_grad_f[], double callback_jac_g[], double callback_hess[]);


	/** The default DLL name of the native implementation (without any platform dependent
	 * prefixes or sufixes) */
	public static final String DLLNAME = "jipopt";
	/** The relative path where the native DLL is found */
	public static final String DLLPATH = "lib";

    static {
        File file = new File(DLLPATH, System.mapLibraryName(DLLNAME));
        System.load(file.getAbsolutePath());
    }

	/** Use C index style for iRow and jCol vectors */
	public final static int C_STYLE = 0;
	/** Use FORTRAN index style for iRow and jCol vectors */
	public final static int FORTRAN_STYLE = 1;

	/* The possible return codes: should be kept in sync with Ipopt return codes */
	public final static int SOLVE_SUCCEEDED = 0;
	public final static int ACCEPTABLE_LEVEL = 1;
	public final static int INFEASIBLE_PROBLEM = 2;
	public final static int SEARCH_DIRECTION_TOO_SMALL = 3;
	public final static int DIVERGING_ITERATES = 4;
	public final static int USER_REQUESTED_STOP = 5;
	public final static int ITERATION_EXCEEDED = -1;
	public final static int RESTORATION_FAILED = -2;
	public final static int ERROR_IN_STEP_COMPUTATION = -3;
	public final static int NOT_ENOUGH_DEGREES_OF_FRE = -10;
	public final static int INVALID_PROBLEM_DEFINITION = -11;
	public final static int INVALID_OPTION = -12;
	public final static int INVALID_NUMBER_DETECTED = -13;
	public final static int UNRECOVERABLE_EXCEPTION = -100;
	public final static int NON_IPOPT_EXCEPTION = -101;
	public final static int INSUFFICIENT_MEMORY = -102;
	public final static int INTERNAL_ERROR = -199;

	/* The possible parameter names: should be kept in sync with Ipopt parameters */
	public final static String KEY_TOL = "tol";
	public final static String KEY_COMPL_INF_TOL = "compl_inf_tol"; 
	public final static String KEY_DUAL_INF_TOL = "dual_inf_tol";
	public final static String KEY_CONSTR_VIOL_TOL = "constr_viol_tol";
	public final static String KEY_ACCEPTABLE_TOL = "acceptable_tol"; 
	public final static String KEY_ACCEPTABLE_COMPL_INF_TOL = "acceptable_compl_inf_tol";
	public final static String KEY_ACCEPTABLE_CONSTR_VIOL_TOL = "acceptable_constr_viol_tol";
	public final static String KEY_ACCEPTABLE_DUAL_INF_TOL= "acceptable_dual_inf_tol"; 
	public final static String KEY_BARRIER_TOL_FACTOR = "barrier_tol_factor";
	public final static String KEY_OBJ_SCALING_FACTOR = "obj_scaling_factor";
	public final static String KEY_BOUND_RELAX_FACTOR = "bound_relax_factor"; 
	public final static String KEY_MAX_ITER = "max_iter";
	public final static String KEY_LIMITED_MEMORY_MAX_HISTORY = "limited_memory_max_history";
	public final static String KEY_FILE_PRINT_LEVEL = "file_print_level";
	public final static String KEY_PRINT_LEVEL = "print_level";
	public final static String KEY_MU_STRATEGY = "mu_strategy";
	public final static String KEY_OUTPUT_FILE = "output_file";
	public final static String KEY_DERIVATIVE_TEST_TOL = "derivative_test_tol";
	public final static String KEY_DERIVATIVE_TEST = "derivative_test";
	public final static String KEY_DERIVATIVE_TEST_PRINT_ALL = "derivative_test_print_all";
	public final static String KEY_PRINT_USER_OPTIONS = "print_user_options";
	public final static String KEY_LINEAR_SOLVER = "linear_solver";

	/** The hessian approximation, set to "limited-memory" if no hessian is available */
	public final static String KEY_HESSIAN_APPROXIMATION = "hessian_approximation";


	/** Pointer to the native optimization object */
	private long ipopt;

	/// Callback arguments
	private double callback_grad_f[];
	private double callback_jac_g[];
	private double callback_hess[];

	private double[]x;
	/** Final value of objective function */
	private double obj_val[] = {0.0};

	/** Values of constraint at final point */
	private double g[];

	/** Final multipliers for lower variable bounds */
	private double mult_x_L[];

	/** Final multipliers for upper variable bounds */
	private double mult_x_U[];

	/** Final multipliers for constraints */
	private double mult_g[];

	/**Status returned by the solver*/
	private int status = INVALID_PROBLEM_DEFINITION;


	/**
	 * Creates a new NLP Solver using {@value #DLLPATH} as path and {@value #DLLNAME}
	 * as the DLL name.
	 *
	 */
	public Ipopt(){
	}

	/** Callback function for the c++ original get_bounds_info function.*/  
	abstract protected boolean get_bounds_info(int n, double[] x_l, double[] x_u,
			int m, double[] g_l, double[] g_u);

	/** Callback function for the  c++ original get_starting_point function. */
	abstract protected boolean get_starting_point(int n, boolean init_x, double[] x,
			boolean init_z, double[] z_L, double[] z_U,
			int m, boolean init_lambda,double[] lambda);

	/** Callback function for the objective function. */
	abstract protected boolean eval_f(int n, double []x, boolean new_x, double []obj_value);
	/** Callback function for the objective function gradient */
	abstract protected boolean eval_grad_f(int n, double []x, boolean new_x, double []grad_f);
	/** Callback function for the constraints */
	abstract protected boolean eval_g(int n, double []x, boolean new_x, int m, double []g);
	/** Callback function for the constraints Jacobian */
	abstract protected boolean eval_jac_g(int n, double []x, boolean new_x,
			int m, int nele_jac, int []iRow, int []jCol, double []values);
	/** Callback function for the hessian */
	abstract protected boolean eval_h(int n, double[] x, boolean new_x, double obj_factor,
			int m, double []lambda, boolean new_lambda,
			int nele_hess, int[] iRow, int []jCol,
			double []values);

	/**
	 * Disposes of the natively allocated memory.
	 * Programmers should, for efficiency, call the dispose method when finished
	 * using a Ipopt object.
	 * <p>
	 * An JIpopt object can be reused to solve different problems by calling again
	 * {@link #create(int, int, int, int, int)}.
	 * In this case, you should call the dispose method only when you
	 * finished with the object and it is not needed anymore.
	 * 	
	 */
	public void dispose(){
		// dispose the native implementation
		if(ipopt!=0){
			FreeIpoptProblem(ipopt);
			ipopt = 0;
		}
	}

	/** Create a new problem. the use is the same as get_nlp_info, change the name for clarity in java.
	 * 
	 * @param n the number of variables in the problem.
	 * @param m the number of constraints in the problem.
	 * @param nele_jac the number of nonzero entries in the Jacobian.
	 * @param nele_hess the number of nonzero entries in the Hessian.
	 * @param index_style the numbering style used for row/col entries in the sparse matrix format(0 for 
	 *  C_STYLE, 1 for FORTRAN_STYLE).
	 *
	 * @return true means success, false means fail! 
	 */
	public boolean create(int n, int m, 
			int nele_jac, int nele_hess, int index_style)
	{
		// delete any previously created native memory
		dispose();

		x=new double[n];
		// allocate the callback arguments
		callback_grad_f = new double[n];
		callback_jac_g = new double[nele_jac];
		callback_hess = new double[nele_hess];

		// the multiplier
		mult_x_U = new double[n];
		mult_x_L = new double[n];
		g = new double[m];
		mult_g = new double[m];

		//	Create the optimization problem and return a pointer to it
		ipopt = CreateIpoptProblem(n, m,  nele_jac, nele_hess, index_style);

		//System.out.println("Finish Java Obj");
		return ipopt == 0 ? false : true;
	}

	/**
	 * Function for adding an integer option.
	 * <p>
	 * The valid keywords are public static members of this class, with names
	 * beginning with <code>KEY_</code>, e.g, {@link #KEY_TOL}.
	 * For more details about the valid options check the Ipopt documentation.
	 * 
	 * @param keyword the option keyword
	 * @param val the value
	 * @return false if the option could not be set (e.g., if keyword is unknown)
	 */
	public boolean setIntegerOption(String keyword, int val){
		return ipopt==0 ? false : AddIpoptIntOption(ipopt, keyword, val);
	}

	/**
	 * Function for adding a number option.
	 * 
	 * @param keyword the option keyword
	 * @param val the value
	 * @return false if the option could not be set (e.g., if keyword is unknown)
	 * 
	 * @see #addIntOption(String, int)
	 */
	public boolean setNumericOption(String keyword, double val){
		return ipopt==0 ? false : AddIpoptNumOption(ipopt, keyword, val);
	}

	/**
	 * Function for adding a string option.
	 * 
	 * @param keyword the option keyword
	 * @param val the value
	 * @return false if the option could not be set (e.g., if keyword is unknown)
	 * 
	 * @see #addIntOption(String, int)
	 */
	public boolean setStringOption(String keyword, String val){
		return ipopt==0 ? false : AddIpoptStrOption(ipopt, keyword, val.toLowerCase());
		//return ipopt==0 ? false : AddIpoptStrOption(ipopt, keyword, val.toLowerCase(Locale.ENGLISH));
	}

	/** This function actually solve the problem.
	 * <p>
	 * The solve status returned is one of the constant fields of this class,
	 * e.g. SOLVE_SUCCEEDED. For more details about the valid solve status
	 * check the Ipopt documentation or the <code>ReturnCodes_inc.h<\code>
	 * which is installed in the Ipopt include directory.	 * 
	 * 
	 * @return the solve status
	 * 
	 * @see #getStatus()
	 */
	public int OptimizeNLP(){
		String outfilename="";//I found input filename has no use in ipopt, future may be corrected!
		this.status= this.OptimizeTNLP( ipopt,outfilename,
				x,g,obj_val,mult_g,mult_x_L,mult_x_U,
				callback_grad_f,callback_jac_g,callback_hess);
		return this.status;
	}

	/**
	 * @return the primal variables at optimal point.
	 */
	public double[] getState() {
		return x;
	}

	/**
	 * @return the final value of the objective function.
	 */
	public double getObjVal() {
		return obj_val[0];
	}

	/**
	 * @return the status of the solver.
	 * 
	 * @see OptimizeNLP()
	 */
	public int getStatus(){
		return status;
	}

	/**
	 * @return Returns the final multipliers for constraints. 
	 */
	public double[] getMultConstraints() {
		return mult_g;
	}

	/**
	 * @return Returns the final multipliers for upper variable bounds.
	 */
	public double[] getMultUpperBounds() {
		return mult_x_U;
	}

	/**
	 * @return Returns the final multipliers for lower variable bounds.
	 */
	public double[] getMultLowerBounds() {
		return mult_x_L;
	}




	///////////////////////////////////////////////////////////////////
	// Below are some additional functions, it can be added more! 
	// such as get_variables_linearity, get_constraints_linearity, get_warm_start_iterate, etc.
	///////////////////////////////////////////////////////////////////

	/** If you using_scaling_parameters=true, please overload this method, 
	 *
	 * @param obj_scaling  =double[1] which you should supply,  negative value means maximize the obj function. 
	 * @param n  the number of variables in the problem, dimension of x.
	 * @param x_scaling  the scaling factors for the variables which are orderd like x, dimension is n. If you
	 *  want IPOPT so scale the variables, you should set use_x_scaling=true in use_x_g_scaling's first element.
	 * @param m  the number of constraints in the problem, dimension of g(x).
	 * @param g_sacling  the scaling factors for the constraints which are orderd like g(x), dimension is m. If
	 *  you want IPOPT so scale the constraints, you should set use_g_scaling=true in use_x_g_scaling's second
	 *  element.
	 * @param use_x_g_scaling =boolean[2]: means {use_x_scaling=true/fasle,use_g_scaling=true/false} which you 
	 *  should supply. 
	 *
	 * @return true means success, false means fail! 
	 */
	public boolean get_scaling_parameters(double[] obj_scaling,
			int n, double[] x_scaling,
			int m, double[] g_scaling,
			boolean[] use_x_g_scaling)
	{
		return false;
	}

	/** If you using_LBFGS("limited-memory"), please overload this method,  
	 *
	 * @return number_of_nonlinear_variables, negtive means no number_of_nonlinear_variables.
	 */
	public int get_number_of_nonlinear_variables(){
		return -1;
	}

	/**  If you using_LBFGS("limited-memory"), please overload this method,  
	 *
	 * @param num_nonlin_vars: number_of_nonlinear_variables, identical with number_of_nonlinear_variables and the 
	 *  length of the array pos_nonlin_vars.
	 * @param pos_nonlin_vars: the indices of all nonlinear variables
	 *
	 * @return true means success, false means fail! 
	 */
	public boolean get_list_of_nonlinear_variables(int num_nonlin_vars,
			int[] pos_nonlin_vars){
		return false;
	}
}
