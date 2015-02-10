package id2208hw2Client;

import id2208hw2bottomup.FlightManager;
import id2208hw2bottomup.FlightManagerService;
import id2208hw2bottomup.FlightPathDTO;
import id2208hw2bottomup.PriceDTO;
import id2208hw2bottomup.TicketsForRouteDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.example.registrated.Authentication;
import org.example.registrated.AuthenticationService;

public class Application {

	private BufferedReader reader;
	private String authToken ="";
	public static void main(String args []) {
		new Application();
	}
	public Application(){
		initiateReader();
		queryUserInput();
	}

	private void initiateReader(){
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	private void queryUserInput(){
		boolean stop = false;
		FlightManagerService flightService = new FlightManagerService();
		FlightManager flightManager = flightService.getFlightManagerPort();
		AuthenticationService authenticationService = new AuthenticationService();
		Authentication authentication = authenticationService.getAuthenticationPort(); 

		try{

			while(!stop){

				String line;
				String departure;
				String destination;
				String date;
				String creditCardNumber;
				printHelp();
				String key = reader.readLine();
				switch (key) {
				case "1":
					if(hasAccess()){
						System.out.println("DepartureCity DestinationCity");
						line = reader.readLine();
						departure = line.split(" ")[0];
						destination = line.split(" ")[1];
						List<FlightPathDTO> dto= flightManager.getPossibleRouting(departure, destination);
						System.out.println("Possible route: ");
						for(FlightPathDTO flight : dto) {
							System.out.println("From: " + flight.getDeparture() + ", to: " + flight.getDestination());
						}
					}
					else{
						System.out.println("Access denied");
					}
					break;
				case "2":
					if(hasAccess()){
						
					
					System.out.println("date as yyyy-mm-dd");
					System.out.println("DepartureCity DestinationCity date");
					line = reader.readLine();
					departure = line.split(" ")[0];
					destination = line.split(" ")[1];
					date = line.split(" ")[2];
					TicketsForRouteDTO ticketsForRouteDTO = flightManager.getTicketsForRoute(departure, 
							destination, date);
					System.out.println("Available tickets: " + ticketsForRouteDTO.getAvailableTickets());
					System.out.println("Price: " + ticketsForRouteDTO.getPrice());
					} 
					else{
						System.out.println("Access denied");
					}
					break;
				case "3":
					if(hasAccess()) {
						
					
					List<PriceDTO> priceDTOList = flightManager.outputPrice();
					for(PriceDTO p : priceDTOList){
						System.out.println("From: " + p.getDepartureCity() + " To: " + p.getDestinationCity() +
								" Price: " + p.getPrice());
					}
					}
					else{
						System.out.println("Access denied");
					}
					break;
				case "4":
					if(hasAccess()){
						
					
					System.out.println("DepatureCity DestinationCity creditcardNumber");
					line = reader.readLine();
					departure = line.split(" ")[0];
					destination = line.split(" ")[1];
					creditCardNumber = line.split(" ")[2];
					String response = flightManager.bookTicket(creditCardNumber, departure, destination);
					System.out.println(response);
					}
					else{
						System.out.println("Access denied");
					}
					break;
				case "5":
					System.out.println("password");
					line = reader.readLine();
					authToken = authentication.getAuthToken(line);
					
					break;
				default:
					System.out.println("Ange en korrekt nyckel");
					break;
				}

			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void printHelp(){
		System.out.println("1: Check availability of route and print path");
		System.out.println("2: Check availability of route at sepcific date, and print ticets available and price");
		System.out.println("3: Price for all available flights");
		System.out.println("4: Book ticket");
		System.out.println("5: Authenticate");
	}
	private boolean hasAccess(){
		return authToken.equals("access_granted");
	}
}
