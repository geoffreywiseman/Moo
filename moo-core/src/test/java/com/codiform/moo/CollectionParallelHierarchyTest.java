package com.codiform.moo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.codiform.moo.annotation.Access;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.CollectionProperty;
import com.codiform.moo.annotation.Ignore;
import com.codiform.moo.curry.Translate;
import com.codiform.moo.translator.TranslationTargetFactory;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class CollectionParallelHierarchyTest {

	@Test
	public void testTranslatesEmployee() {
		// Using: http://monanews.info/wp-content/uploads/2017/12/creative-organization-chart-creative-organization-chart-ideas-for-presentations-free.jpg
		Employee vpSales = new Employee( 3, "VP, Sales", 200000 );
		Employee coo = new Manager( 2, "COO", 250000, Collections.singletonList( vpSales ) );
		Employee svpCounsel = new Employee( 4, "SVP, General Counsel", 225000 );
		Employee ceo = new Manager( 1, "CEO", 300000, Arrays.asList( coo, svpCounsel ) );
		EmployeeDto dto = Translate.to( ManagerDto.class ).from( ceo );
		assertEmployeeEquality( dto, ceo );
	}

	private void assertEmployeeEquality( EmployeeDto dto, Employee domain ) {
		Class<? extends EmployeeDto> expectedClass = domain instanceof Manager ? ManagerDto.class : EmployeeDto.class;
		assertThat( dto, instanceOf( expectedClass ) );
		assertThat( dto.getEmployeeNumber(), equalTo( domain.getEmployeeNumber() ) );
		assertThat( dto.getName(), equalTo( domain.getName() ) );
		if ( domain instanceof Manager ) {
			List<Employee> domainReports = ( (Manager)domain ).getDirectReports();
			assertThat( domainReports, Matchers.notNullValue() );

			List<EmployeeDto> dtoReports = ( (ManagerDto)dto ).getDirectReports();
			assertThat( dtoReports, Matchers.notNullValue() );

			assertThat( "EmployeeDto '" + dto.getName() + "' directReports.size() doesn't match domain",
					dtoReports.size(), equalTo( domainReports.size() ) );
			for ( int index = 0; index < domainReports.size(); index++ )
				assertEmployeeEquality( dtoReports.get( index ), domainReports.get( index ) );
		}
	}

	public static class Employee {
		private int employeeNumber;
		private String name;

		@Ignore( "Sensitive information" )
		private int salary;

		Employee( int employeeNumber, String name, int salary ) {
			this.employeeNumber = employeeNumber;
			this.name = name;
			this.salary = salary;
		}

		int getEmployeeNumber() {
			return employeeNumber;
		}

		public String getName() {
			return name;
		}

		public int getSalary() {
			return salary;
		}

		@Override
		public String toString() {
			return "Employee{" +
					"employeeNumber=" + employeeNumber +
					", name='" + name + '\'' +
					'}';
		}
	}

	@Access( AccessMode.FIELD )
	public static class EmployeeDto {
		private int employeeNumber;
		private String name;

		public EmployeeDto() {
		}

		@Override
		public String toString() {
			return "EmployeeDto{" +
					"employeeNumber=" + employeeNumber +
					", name='" + name + '\'' +
					'}';
		}

		int getEmployeeNumber() {
			return employeeNumber;
		}

		public String getName() {
			return name;
		}
	}

	public static class Manager extends Employee {
		private List<Employee> directReports;

		public Manager( int employeeNumber, String name, int salary, List<Employee> directReports ) {
			super( employeeNumber, name, salary );
			this.directReports = directReports;
		}

		List<Employee> getDirectReports() {
			return directReports;
		}
	}

	public static class ManagerDto extends EmployeeDto {

		@CollectionProperty( itemClass = EmployeeDto.class, itemFactory = EmployeeFactory.class )
		private List<EmployeeDto> directReports = new ArrayList<>();

		List<EmployeeDto> getDirectReports() {
			return directReports;
		}
	}

	public static class EmployeeFactory implements TranslationTargetFactory {

		@SuppressWarnings( "unchecked" )
		@Override
		public <T> T getTranslationTargetInstance( Object source, Class<T> targetType ) {
			if ( EmployeeDto.class.equals( targetType ) ) {
				if ( source instanceof Manager )
					return (T)new ManagerDto();
				else
					return (T)new EmployeeDto();
			} else {
				return null;
			}
		}

	}
}
