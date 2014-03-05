package coordinate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridInitialiser {
	
	private static final String EMPTY_CELL = "E";
	private static final String NOT_USED = "XX";
	private static final double MATRIX_DISPLACEMENT = 20;

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
			Point leftUpPoint = new Point(x-1 * MATRIX_DISPLACEMENT, y-1 * MATRIX_DISPLACEMENT);
			Point rightUpPoint = new Point(x+1 * MATRIX_DISPLACEMENT, y-1 * MATRIX_DISPLACEMENT);
			Point localPoint = new Point(x * MATRIX_DISPLACEMENT, y * MATRIX_DISPLACEMENT);
			leftUpMarker.setPoint(leftUpPoint);
			rightUpMarker.setPoint(rightUpPoint);
			localMarker.setPoint(localPoint);
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
			Point leftDownPoint = new Point((x-1) * MATRIX_DISPLACEMENT, (y+1) * MATRIX_DISPLACEMENT);
			Point rightDownPoint = new Point((x+1) * MATRIX_DISPLACEMENT, (y+1) * MATRIX_DISPLACEMENT);
			Point localPoint = new Point(x * MATRIX_DISPLACEMENT, y * MATRIX_DISPLACEMENT);
			leftDownMarker.setPoint(leftDownPoint);
			rightDownMarker.setPoint(rightDownPoint);
			localMarker.setPoint(localPoint);
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
		BufferedReader reader =  new BufferedReader(new FileReader(withExtension));
		List<String[]> matrix = new ArrayList<String[]>();
		String input = reader.readLine();
		boolean shortRow = false;
		while (input != null)
		{
			String[] tokens = input.split(", ");
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
