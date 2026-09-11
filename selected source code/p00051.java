import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Main{
	public static void main(String[] args){
		BufferedReader bfr=new BufferedReader(new InputStreamReader(System.in));
		String str="";
		int[] hairetu=new int[8];
		String max="",min="";

		try {
			int d_set;
			str=bfr.readLine();
			d_set=Integer.parseInt(str);

			for(int i=0; i<d_set; i++) {
				str="";
				min="";
				max="";
				str=bfr.readLine();
				for(int j=0; j<8; j++) {
					hairetu[j]=Integer.parseInt(str.charAt(j)+"");
				}
				hairetu=mSort(hairetu);

				for(int k=0; k<8; k++) {
					min=min+""+hairetu[k];
					max=max+""+hairetu[7-k];
				}
				System.out.println(Integer.parseInt(max)-Integer.parseInt(min));
			}
		}catch(IOException e) {

		}
	}

	public static int[] mSort(int array[])  {
		if(array.length > 1) {
			int elementsInA1 = array.length/2;
			int elementsInA2 = array.length - elementsInA1;
			int arr1[] = new int[elementsInA1];
			int arr2[] = new int[elementsInA2];

			for(int i = 0; i < elementsInA1; i++)
				arr1[i] = array[i];

			for(int i = elementsInA1; i < elementsInA1 + elementsInA2; i++)
				arr2[i - elementsInA1] = array[i];

			arr1 = mSort(arr1);
			arr2 = mSort(arr2);

			int i = 0, j = 0, k = 0;

			while(arr1.length != j && arr2.length != k) {
				if(arr1[j] <= arr2[k]) {
					array[i] = arr1[j];
					i++;
					j++;
				} else {
					array[i] = arr2[k];
					i++;
					k++;
				}
			}

			while(arr1.length != j) {
				array[i] = arr1[j];
				i++;
				j++;
			}
			while(arr2.length != k) {
				array[i] = arr2[k];
				i++;
				k++;
			}
		}
		return array;
	}
}
