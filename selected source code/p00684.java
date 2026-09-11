import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	
	//public static final int NUMBER  = 0;
	public static final long ADD     = -1;
	public static final long SUB     = -2;
	public static final long MUL     = -3;
	public static final long IMG     = -4;
	public static final long KAKKO_L = -5;
	public static final long KAKKO_R = -6;
	
	public static final Token ADD_TOK = new Token("+", ADD);
	public static final Token SUB_TOK = new Token("-", SUB);
	public static final Token MUL_TOK = new Token("*", MUL);
	public static final Token IMG_TOK = new Token("i", IMG);
	public static final Token KAKKO_L_TOK = new Token("(", KAKKO_L);
	public static final Token KAKKO_R_TOK = new Token(")", KAKKO_R);
	
	public static class Token{
		String value;
		long type;
		
		public Token(String value, long type) {
			super();
			this.value = value;
			this.type = type;
		}
		
		public String toString(){
			return "[ " + this.value + " " + this.type + " ]";
		}
	}
	
	public static final int OVER = 10000;
	public static final Complex IMG_I = new Complex(0, 1); 
	
	public static class Complex{
		long real, img;

		public Complex(long real, long img) {
			super();
			this.real = real;
			this.img = img;
		}
		
		public boolean is_overflow(){
			return false;
		}
		
		public Complex add(Complex another){
			if(another.is_overflow()){
				return another;
			}
			
			final long n_real = this.real + another.real;
			final long n_img  = this.img  + another.img;
			
			if(Math.abs(n_real) <= OVER && Math.abs(n_img) <= OVER){
				return new Complex(n_real, n_img);
			}else{
				return new Overflow();
			}
		}
		
		public Complex sub(Complex another){
			if(another.is_overflow()){
				return another;
			}
			
			final long n_real = this.real - another.real;
			final long n_img  = this.img  - another.img;
			
			if(Math.abs(n_real) <= OVER && Math.abs(n_img) <= OVER){
				return new Complex(n_real, n_img);
			}else{
				return new Overflow();
			}
		}
		
		public Complex mul(Complex another){
			if(another.is_overflow()){
				return another;
			}
			
			final long n_real = this.real * another.real - this.img * another.img;
			final long n_img  = this.real * another.img + this.img * another.real;
			
			if(Math.abs(n_real) <= OVER && Math.abs(n_img) <= OVER){
				return new Complex(n_real, n_img);
			}else{
				return new Overflow();
			}
		}
		
		public String toString(){
			if(is_overflow()){
				return "overflow";
			}else if(this.img == 0){
				return this.real + "";
			}else if(this.real == 0){
				return this.img + "i";
			}else{
				return this.real + (this.img < 0 ? "" : "+") +  this.img + "" + "i"; 
			}
		}
	}
	
	public static class Overflow extends Complex{

		public Overflow() {
			super(0, 0);
		}
		
		public boolean is_overflow(){
			return true;
		}
		
		public Complex add(Complex another){
			return this;
		}
		
		public Complex sub(Complex another){
			return this;
		}
		
		public Complex mul(Complex another){
			return this;
		}
	}
	
	public static interface Expr{
		public Complex eval();
	}
	
	public static class Inner implements Expr{
		Expr inner;

		public Inner(Expr inner) {
			super();
			this.inner = inner;
		}
		
		@Override
		public Complex eval() {
			return inner.eval();
		}
		
		@Override
		public String toString(){
			return "(" + inner +  ")";
		}
	}
	
	public static class Plus implements Expr{
		Expr left, right;

		public Plus(Expr left, Expr right) {
			super();
			this.left = left;
			this.right = right;
		}

		@Override
		public Complex eval() {
			return left.eval().add(right.eval());
		}
		
		@Override
		public String toString(){
			return left + "+" + right;
		}
	}
	
	public static class Minus implements Expr{
		Expr left, right;

		public Minus(Expr left, Expr right) {
			super();
			this.left = left;
			this.right = right;
		}

		@Override
		public Complex eval() {
			return left.eval().sub(right.eval());
		}
		
		@Override
		public String toString(){
			return left + "-" + right;
		}
	}
	
	public static class Mul implements Expr{
		Expr left, right;

		public Mul(Expr left, Expr right) {
			super();
			this.left = left;
			this.right = right;
		}

		@Override
		public Complex eval() {
			//System.out.println(left.eval().mul(right.eval()));
			
			return left.eval().mul(right.eval());
		}
		
		@Override
		public String toString(){
			return left + "*" + right;
		}
	}
	
	public static class Const implements Expr{
		Complex cons;
		
		public Const(Complex cons){
			this.cons = cons;
		}
		
		@Override
		public Complex eval(){
			return cons;
		}
		
		@Override
		public String toString(){
			return cons + "";
		}
	}
	
	//+, -
	public static Expr parse_1(LinkedList<Token> tokens){
		Expr left = parse_2(tokens);
		
		if(left == null){
			return null;
		}
		
		while(!tokens.isEmpty()){
			if(!(tokens.peek().type == ADD) && !(tokens.peek().type == SUB)){
				return left;
			}
			
			long type = tokens.poll().type;
			Expr right = parse_2(tokens);
			
			if(type == ADD){
				left = new Plus(left, right);
			}else{
				left = new Minus(left, right);
			}
		}
		
		return left;
	}
	
	//*, /
	public static Expr parse_2(LinkedList<Token> tokens){
		Expr left = parse_3(tokens);
		
		if(left == null){
			return null;
		}
		
		while(!tokens.isEmpty()){
			if(!(tokens.peek().type == MUL)){
				return left;
			}
			
			long type = tokens.poll().type;
			Expr right = parse_3(tokens);
			
			if(type == MUL){
				left = new Mul(left, right);
			}

		}
		
		return left;
	}
	
	//primitive, ()
	public static Expr parse_3(LinkedList<Token> tokens){
		//System.out.println(tokens.peek());
		
		if(tokens.isEmpty()){
			return null;
		}else if(tokens.peek().type >= 0){
			final long number = tokens.poll().type;
			return new Const(number > OVER ? new Overflow() : new Complex(number, 0));
		}else if(tokens.peek().type == IMG){
			tokens.poll();
			return new Const(IMG_I);
		}else if(tokens.peek().type == KAKKO_L){
			tokens.poll();
			Expr expr = parse_1(tokens);
			
			if(tokens.peek().type == KAKKO_R){
				tokens.poll();
				return new Inner(expr);
			}else{
				return null;
			}			
		}
		
		return null;
	}
	
	public static void add_stack(String line, LinkedList<Token> stack){
		char[] input = line.toCharArray();
		
		long count = -1;
		for(char c : input){
			if('0' <= c && c <= '9'){
				if(count == -1){
					count = 0;
				}
				
				count *= 10;
				count += c - '0';
			}else{
				if(count != -1){
					stack.add(new Token("", count));
				}
				count = -1;
			}
			
			switch(c){
			case '+':
				stack.add(ADD_TOK);
				break;
			case '-':
				stack.add(SUB_TOK);
				break;
			case '*':
				stack.add(MUL_TOK);
				break;
			case 'i':
				stack.add(IMG_TOK);
				break;
			case '(':
				stack.add(KAKKO_L_TOK);
				break;
			case ')':
				stack.add(KAKKO_R_TOK);
				break;
			}
			
			//System.out.println(stack);
		}
		
		if(count != -1){
			stack.add(new Token("", count));
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		LinkedList<Token> stack = new LinkedList<Token>();
		
		while(sc.hasNextLine()){
			String input = sc.nextLine();
			stack.clear();
			
			add_stack(input, stack);
			try{
				Expr node = parse_1(stack);
				
				//System.out.println(node);
				
				System.out.println(node.eval());
			}catch(NullPointerException e){
				System.err.println(input);
			}
		}
		
		sc.close();
	}
}
