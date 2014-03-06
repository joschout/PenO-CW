package coordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridInitialiser {
	
	private static final String EMPTY_CELL = "E";
	private static final String NOT_USED = "XX";
	private static final double X_DISPLACEMENT = 20;
	private static final double Y_DISPLACEMENT = 20 * Math.sqrt(3);

	public Grid readGrid(String filename) throws IOException
	{
		List<String[]> matrix = constructMatrix(filename);
		List<GridTriangle> triangles = new ArrayList<GridTriangle>();
		for (int y = 0; y < matrix.size(); y++) // y is vertikale index
		{
			for (int x = 0; x < matrix.get(y).length; x++) // x is horizontale index
			{
				constructUpperTriangle(matrix, triangles, y, x);
				constructLowerTriangle(matrix, triangles, y ,x);
			}
		}
		return new Grid(triangles);
	}

	// maakt een down-facing driehoek
	private void constructUpperTriangle(List<String[]> matrix,
			List<GridTriangle> triangles, int y, int x) {
		try
		{
			String leftUpToken = matrix.get(y-1)[x-1];
			if (! representsShape(leftUpToken))
				return;
			GridMarker leftUpMarker = parseFileShape(leftUpToken);
			String rightUpToken = matrix.get(y-1)[x+1];
			if (! representsShape(rightUpToken))
				return;
			GridMarker rightUpMarker = parseFileShape(rightUpToken);
			String localToken = matrix.get(y)[x];
			if (! representsShape(localToken))
				return;
			GridMarker localMarker = parseFileShape(localToken);
			GridPoint leftUpPoint = new GridPoint((x-1) * X_DISPLACEMENT, (y-1) * Y_DISPLACEMENT);
			GridPoint rightUpPoint = new GridPoint((x+1) * X_DISPLACEMENT, (y-1) * Y_DISPLACEMENT);
			GridPoint localPoint = new GridPoint(x * X_DISPLACEMENT, y * Y_DISPLACEMENT);
			leftUpMarker.setPoint(leftUpPoint);
			rightUpMarker.setPoint(rightUpPoint);
			localMarker.setPoint(localPoint);
			leftUpMarker.setOrientation(MarkerOrientation.LEFT);
			rightUpMarker.setOrientation(MarkerOrientation.RIGHT);
			localMarker.setOrientation(MarkerOrientation.DOWN);
			List<GridMarker> markers = new ArrayList<GridMarker>();
			markers.add(leftUpMarker);
			markers.add(rightUpMarker);
			markers.add(localMarker);
			triangles.add(new GridTriangle(markers));
		}
		catch (IndexOutOfBoundsException e)
		{
			return;
		}
	}
	
	// maakt een up-facing driehoek
	private void constructLowerTriangle(List<String[]> matrix,
			List<GridTriangle> triangles, int y, int x)
	{
		try
		{
			String leftDownToken = matrix.get(y+1)[x-1];
			if (! representsShape(leftDownToken))
				return;
			GridMarker leftDownMarker = parseFileShape(leftDownToken);
			String rightDownToken = matrix.get(y+1)[x+1];
			if (! representsShape(rightDownToken))
				return;
			GridMarker rightDownMarker = parseFileShape(rightDownToken);
			String localToken = matrix.get(y)[x];
			if (! representsShape(localToken))
				return;
			GridMarker localMarker = parseFileShape(localToken);
			GridPoint leftDownPoint = new GridPoint((x-1) * X_DISPLACEMENT, (y+1) * Y_DISPLACEMENT);
			GridPoint rightDownPoint = new GridPoint((x+1) * X_DISPLACEMENT, (y+1) * Y_DISPLACEMENT);
			GridPoint localPoint = new GridPoint(x * X_DISPLACEMENT, y * Y_DISPLACEMENT);
			leftDownMarker.setPoint(leftDownPoint);
			rightDownMarker.setPoint(rightDownPoint);
			localMarker.setPoint(localPoint);
			leftDownMarker.setOrientation(MarkerOrientation.LEFT);
			rightDownMarker.setOrientation(MarkerOrientation.RIGHT);
			localMarker.setOrientation(MarkerOrientation.UP);
			List<GridMarker> markers = new ArrayList<GridMarker>();
			markers.add(leftDownMarker);
			markers.add(rightDownMarker);
			markers.add(localMarker);
			triangles.add(new GridTriangle(markers));
		}
		catch (IndexOutOfBoundsException e)
		{
			return;
		}
	}

	private List<String[]> constructMatrix(String filename) throws FileNotFoundException,
			IOException {
		String withExtension = filename + ".txt";
		File file = new File(withExtension);
		if (! file.exists() || file.isDirectory())
		{
			withExtension = filename + ".csv";
			file = new File(withExtension);
		}
		BufferedReader reader =  new BufferedReader(new FileReader(file));
		List<String[]> matrix = new ArrayList<String[]>();
		String input = reader.readLine();
		boolean shortRow = false;
		while (input != null)
		{
			String[] tokens = input.split(", ");
			if (tokens.length <= 1)
			{
				tokens = input.split(",");
			}
			String[] matrixRow = new String[tokens.length * 2];
			int tokenCursor = 0;
			if (shortRow)
			{
				for (int i = 0; i < matrixRow.length; i++)
				{
					if (i % 2 == 0)
					{
						matrixRow[i] = EMPTY_CELL;
					}
					else
					{
						matrixRow[i] = tokens[tokenCursor++];
					}
				}
				shortRow = false;
			}
			else
			{
				for (int i = 0; i < matrixRow.length; i++)
				{
					if (i % 2 == 0)
					{
						matrixRow[i] = tokens[tokenCursor++];
					}
					else
					{
						matrixRow[i] = EMPTY_CELL;
					}
				}
				shortRow = true;
			}
			matrix.add(matrixRow);
			input = reader.readLine();
		}
		reader.close();
		return matrix;
	}
	
	private GridMarker parseFileShape(String fileShape)
	{
		char colour = fileShape.charAt(0);
		char shape = fileShape.charAt(1);
		String markerColour = decideColour(colour);
		String markerShape = decideShape(shape);
		return new GridMarker(markerColour, markerShape);
	}
	
	private String decideShape(char shape)
	{
		switch (shape)
		{
		case 'R':
			return "rectangle";
		case 'O':
			return "oval";
		case 'S':
			return "star";
		default:
			return "heart";
		}
	}
	
	private String decideColour(char colour)
	{
		switch (colour)
		{
		case 'R':
			return "red";
		case 'Y':
			return "yellow";
		case 'W':
			return "white";
		case 'B':
			return "blue";
		default:
			return "green";
		}
	}
	
	private boolean representsShape(String token)
	{
		return ! (token.equals(EMPTY_CELL) || token.equals(NOT_USED));
	}
}
