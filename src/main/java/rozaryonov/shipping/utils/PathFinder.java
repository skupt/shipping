package rozaryonov.shipping.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import rozaryonov.shipping.model.LogisticNetElement;

/**
 * Use:
 * PathFinder pathFinder = new PathFinder(logisticNetElementService, netConfigLong);
 * String path = pathFinder.showShortestPath(startTownIdLong, finishTownIdLong);
 * Double distance =  pathFinder.calcMinDistance(startTownIdLong, finishTownIdLong);
 * String path2 = pathFinder.showShortestPath(startTownIdLong2, finishTownIdLong2);
 * Double distance2 =  pathFinder.calcMinDistance(startTownIdLong2, finishTownIdLong2);
 */
public class PathFinder {
	
	private Set<Town> townSet;
	
	public PathFinder(Iterable<LogisticNetElement> logisticNetElements) {
		townSet= initTownSet(logisticNetElements);
	}
	
	private static Set<Town> initTownSet(Iterable<LogisticNetElement> logisticNetElements) {
		Map<String, Town> townMap = new TreeMap<>();
		Town town1;
		Town town2;
		for (LogisticNetElement e : logisticNetElements) {
			townMap.putIfAbsent(e.getCity().getId() + "", new Town(e.getCity().getId(), e.getCity().getName()));
			town1 = townMap.get(e.getCity().getId() + "");

			townMap.putIfAbsent(e.getNeighbor().getId() + "",
					new Town(e.getNeighbor().getId(), e.getNeighbor().getName()));
			town2 = townMap.get(e.getNeighbor().getId() + "");

			Double distance = e.getDistance();

			town1.neighbors.put(town2, distance);
		}
		Set<Town> result = new TreeSet<>();
		for (Map.Entry<String, Town> townTown : townMap.entrySet())
			result.add(townTown.getValue());

		return result;
	}

	public String showShortestPath(long townStartId, long townFinishId) throws ClassNotFoundException, IOException {
		Set<Town> townSetCloned = cloneViaSerialization((TreeSet<Town>) townSet);
		Town start = null, finish = null;
		Iterator<Town> iter = townSetCloned.iterator();
		while (iter.hasNext()) {
			Town ct = iter.next();
			if (ct.id == townStartId) {
				start = ct;
				start.distance = 0;
			}
			if (ct.id == townFinishId)
				finish = ct;
		}
		if (start == null || finish == null)
			throw new NoSuchElementException("Start or finish towns are absent in towns' queue");
		if (start.equals(finish)) return "" + start.name + " --> " + start.name;
		calcShortestDistances(townSetCloned, finish);
		TreeSet<Town> path = findShortestPath(townSetCloned, start, finish);
		double distAcc=0;
		StringBuilder sb = new StringBuilder();
		for (Town town : path) {
			sb.append(town.name).append(" (").append(town.distance-distAcc).append(")").append(" --> ");
			distAcc += town.distance;
		}
		sb.delete(sb.length()-5, sb.length());

		return sb.toString();
	}
	
	public Double calcMinDistance(long townStartId, long townFinishId) throws ClassNotFoundException, IOException {
		if (townStartId==townFinishId) return 0.0;
		Set<Town> townSetCloned = cloneViaSerialization((TreeSet<Town>) townSet);
		Town start = null, finish = null;
		Iterator<Town> iter = townSetCloned.iterator();
		while (iter.hasNext()) {
			Town ct = iter.next();
			if (ct.id == townStartId) {
				start = ct;
				start.distance = 0;
			}
			if (ct.id == townFinishId)
				finish = ct;
		}
		if (start == null || finish == null)
			throw new NoSuchElementException("Start or finish towns are absent in towns' queue");
		calcShortestDistances(townSetCloned, finish);
		double dist = findShortestPath(townSetCloned, start, finish).last().distance;
		return dist;
	}

	
	void calcShortestDistances(Set<Town> setTown, Town finish) {

		for (int i = 0; i < setTown.size(); i++) {
			Town currentTown = Collections.min(setTown, (Town a, Town b) -> {
				int result = Boolean.compare(a.invited, b.invited);
				if (result == 0)
					result = Double.compare(a.distance, b.distance);
				return result;
			});
			// получаем непосещенную вершину с минимальным расстоянием до нее
			for (Map.Entry<Town, Double> townNeighbor : currentTown.neighbors.entrySet()) {// цикл расчета и установки
				// расстояний до соседей
				if (townNeighbor.getKey().invited) {
					continue;// если точка уже посещена, то перейти к следующему соседу
				}
				Double nextNeibhorDist = currentTown.distance + townNeighbor.getValue();// расчет расстояния до соседа
				if (nextNeibhorDist < townNeighbor.getKey().distance) {
					townNeighbor.getKey().distance = nextNeibhorDist;// если из
				}
				// текущей точки до сосеней общее расстояние меньше чем установленное ранее, то
				// установить меньшее
			}
			currentTown.invited = true;
			if (currentTown == finish)
				// если вершина - это конец пути, то прервать цикл
				break;
		}
	}

	private TreeSet<Town> findShortestPath(Set<Town> setTown, Town startT, Town finishT) {
		TreeSet<Town> pathOverTowns = new TreeSet<>();
		pathOverTowns.add(finishT);
		Town currentTown = finishT;
		for (int i = 0; i < setTown.size(); i++) {
			ArrayList<Town> distEqualTown = new ArrayList<>();
			Town nextTownInPath = null;
			for (Map.Entry<Town, Double> nb : currentTown.neighbors.entrySet()) {
				if ((nb.getKey().distance) + nb.getValue() == currentTown.distance) {
					distEqualTown.add(nb.getKey());
				}
			}
			nextTownInPath = distEqualTown.get(0);
			pathOverTowns.add(nextTownInPath);
			if (nextTownInPath == startT)
				break;
			currentTown = nextTownInPath;
		}
		return pathOverTowns;
	}
	
	public static class Town implements Comparable<Town>, Serializable {
		private static final long serialVersionUID = 1L;
		private long id;
		private String name;
		private double distance;
		private boolean invited;
		private Map<Town, Double> neighbors;

		public Town(String name) {
			this.name = name;
			this.distance = Double.POSITIVE_INFINITY;
			this.invited = false;
			neighbors = new TreeMap<>();
		}

		public Town(long id, String name) {
			this.id = id;
			this.name = name;
			this.distance = Double.POSITIVE_INFINITY;
			this.invited = false;
			neighbors = new TreeMap<>();
		}

		public int compareTo(Town other) {
			int result = Double.compare(this.distance, other.distance);
			if (result == 0)
				result = Boolean.compare(this.invited, other.invited);
			if (result == 0)
				result = this.name.compareTo(other.name);

			return result;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (id ^ (id >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Town other = (Town) obj;
			if (id != other.id)
				return false;
			return true;
		}

		public String toString() {
			Town t = this;
			StringBuilder sb = new StringBuilder();
			sb.append(t.getClass().getSimpleName()).append(": ");
			sb.append(t.name).append("; ").append(t.distance).append("; invited=");
			sb.append(t.invited).append('\n');
			for (Map.Entry<Town, Double> entry : t.neighbors.entrySet()) {
				sb.append(entry.getKey().name).append("; ").append(entry.getValue()).append(';').append('\n');
			}
			return sb.toString();
		}

	}


	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T cloneViaSerialization(T willBeenCloned) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream out = new ObjectOutputStream(baos);) {
			out.writeObject(willBeenCloned);
		}
		T clonedObj;
		try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));) {
			clonedObj = (T) in.readObject(); 
		}
		return clonedObj;
	}
}
