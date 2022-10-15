package com.example.moviesapp.data.entities;

import java.util.List;

public class MovieResponse {
	private Dates dates;
	private int page;
	private int totalPages;
	private List<Movie> results;
	private int totalResults;


	public Dates getDates(){
		return dates;
	}

	public int getPage(){
		return page;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public List<Movie> getResults(){
		return results;
	}

	public int getTotalResults(){
		return totalResults;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"dates = '" + dates + '\'' + 
			",page = '" + page + '\'' + 
			",total_pages = '" + totalPages + '\'' + 
			",results = '" + results + '\'' + 
			",total_results = '" + totalResults + '\'' + 
			"}";
		}
}